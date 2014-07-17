package org.gbif.checklistbank.cli.normalizer;

import com.google.common.util.concurrent.Service;
import org.gbif.cli.Command;
import org.gbif.cli.service.ServiceCommand;
import org.kohsuke.MetaInfServices;

@MetaInfServices(Command.class)
public class NormalizerCommand extends ServiceCommand {

  private final NormalizerConfiguration configuration = new NormalizerConfiguration();

  public NormalizerCommand() {
    super("normalizer");
  }

  @Override
  protected Service getService() {
    return new NormalizerService(configuration);
  }

  @Override
  protected Object getConfigurationObject() {
    return configuration;
  }

}