package org.gbif.checklistbank.cli.registry;

import org.gbif.api.model.registry.Dataset;
import org.gbif.api.vocabulary.DatasetType;
import org.gbif.checklistbank.cli.common.NeoConfiguration;
import org.gbif.checklistbank.cli.common.RabbitBaseService;
import org.gbif.checklistbank.index.guice.RealTimeModule;
import org.gbif.checklistbank.index.guice.Solr;
import org.gbif.checklistbank.logging.LogContext;
import org.gbif.checklistbank.model.DatasetCore;
import org.gbif.checklistbank.service.DatasetImportService;
import org.gbif.checklistbank.service.mybatis.guice.InternalChecklistBankServiceMyBatisModule;
import org.gbif.checklistbank.service.mybatis.guice.Mybatis;
import org.gbif.checklistbank.service.mybatis.mapper.DatasetMapper;
import org.gbif.common.messaging.api.messages.RegistryChangeMessage;
import org.gbif.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.inject.Key;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A service that watches registry changed messages and does deletions of checklists and
 * updates to the dataset title table in CLB.
 */
public class RegistryService extends RabbitBaseService<RegistryChangeMessage> {


  private static final Logger LOG = LoggerFactory.getLogger(RegistryService.class);

  private final RegistryConfiguration cfg;
  private final DatasetImportService solrService;
  private final DatasetImportService mybatisService;
  private final DatasetMapper datasetMapper;
  private Timer timerSolr;
  private Timer timerSql;

  public RegistryService(RegistryConfiguration cfg) {
    super("clb-registry-change", cfg.poolSize, cfg.messaging, cfg.ganglia, InternalChecklistBankServiceMyBatisModule.create(cfg.clb), new RealTimeModule(cfg.solr));
    this.cfg = cfg;

    // init mybatis layer and solr from cfg instance
    solrService = getInstance(Key.get(DatasetImportService.class, Solr.class));
    mybatisService = getInstance(Key.get(DatasetImportService.class, Mybatis.class));
    datasetMapper = getInstance(DatasetMapper.class);
  }

  @Override
  protected void initMetrics(MetricRegistry registry) {
    super.initMetrics(registry);
    timerSolr = registry.timer(regName("solr.time"));
    timerSql = registry.timer(regName("sql.time"));
  }

  /**
   * Deletes all neo and kvp files created during indexing.
   *
   * @param cfg        a neo configuration needed to point to the right setup
   * @param datasetKey the dataset to delete files for
   */
  public static void deleteStorageFiles(NeoConfiguration cfg, UUID datasetKey) {
    // delete neo & kvp storage files
    File kvp = cfg.kvp(datasetKey);
    if (kvp.exists() && !kvp.delete()) {
      LOG.warn("Failed to delete kvo data dir {}", kvp.getAbsoluteFile());
    }

    // delete neo storage files
    File neoDir = cfg.neoDir(datasetKey);
    if (neoDir.exists()) {
      try {
        FileUtils.deleteDirectory(neoDir);
      } catch (IOException e) {
        LOG.warn("Failed to delete neo data dir {}", neoDir.getAbsoluteFile());
      }
    }
    LOG.info("Deleted dataset storage files");
  }

  private void delete(UUID key) throws RuntimeException {
    LogContext.startDataset(key);
    LOG.info("Deleting data for checklist {}", key);
    // solr
    Timer.Context context = timerSolr.time();
    try {
      solrService.deleteDataset(key);
    } catch (Throwable e) {
      LOG.error("Failed to delete dataset from solr", key, e);
    } finally {
      context.stop();
    }

    // postgres usage
    context = timerSql.time();
    try {
      mybatisService.deleteDataset(key);
    } catch (Throwable e) {
      LOG.error("Failed to delete dataset from postgres", key, e);
    } finally {
      context.stop();
    }

    // archives
    deleteStorageFiles(cfg.neo, key);

    // delete dataset table entry
    datasetMapper.delete(key);
    LogContext.endDataset();
  }

  @Override
  public void handleMessage(RegistryChangeMessage msg) {
    if (Dataset.class.equals(msg.getObjectClass())) {
      Dataset d = (Dataset) ObjectUtils.coalesce(msg.getNewObject(), msg.getOldObject());
      if (d != null && DatasetType.CHECKLIST == d.getType()) {
        DatasetCore dc = new DatasetCore(d);
        switch (msg.getChangeType()) {
          case DELETED:
            delete(d.getKey());
            break;
          case UPDATED:
            datasetMapper.update(dc);
            break;
          case CREATED:
            datasetMapper.insert(dc);
            break;
        }
      }
    }
  }

  @Override
  public Class<RegistryChangeMessage> getMessageClass() {
    return RegistryChangeMessage.class;
  }
}
