package org.gbif.checklistbank.cli.matcher;

import org.gbif.checklistbank.config.ClbConfiguration;
import org.gbif.checklistbank.config.GangliaConfiguration;
import org.gbif.common.messaging.config.MessagingConfiguration;
import org.gbif.common.search.solr.SolrConfig;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

/**
 *
 */
@SuppressWarnings("PublicField")
public class MatcherConfiguration {


  @Parameter(names = "--pool-size")
  @Min(1)
  public int poolSize = 1;

  @ParametersDelegate
  @Valid
  @NotNull
  public GangliaConfiguration ganglia = new GangliaConfiguration();

  @ParametersDelegate
  @NotNull
  @Valid
  public MessagingConfiguration messaging = new MessagingConfiguration();

  @ParametersDelegate
  @Valid
  @NotNull
  public ClbConfiguration clb = new ClbConfiguration();

  @ParametersDelegate
  @Valid
  @NotNull
  public SolrConfig solr = new SolrConfig();

}
