package org.gbif.checklistbank.service.mybatis;

import org.gbif.api.model.checklistbank.SpeciesProfile;
import org.gbif.api.service.checklistbank.SpeciesProfileService;
import org.gbif.checklistbank.service.mybatis.mapper.SpeciesProfileMapper;

import com.google.inject.Inject;

/**
 * Implements a SpeciesProfileService using MyBatis.
 */
public class SpeciesProfileServiceMyBatis extends NameUsageComponentServiceMyBatis<SpeciesProfile>
  implements SpeciesProfileService {

  @Inject
  SpeciesProfileServiceMyBatis(SpeciesProfileMapper speciesProfileMapper) {
    super(speciesProfileMapper);
  }
}
