package org.gbif.checklistbank.registry;

import org.gbif.api.model.common.paging.Pageable;
import org.gbif.api.model.common.paging.PagingResponse;
import org.gbif.api.model.registry.Dataset;
import org.gbif.api.model.registry.Installation;
import org.gbif.api.model.registry.Node;
import org.gbif.api.model.registry.Organization;
import org.gbif.api.model.registry.search.KeyTitleResult;
import org.gbif.api.service.registry.NodeService;
import org.gbif.api.vocabulary.Country;
import org.gbif.api.vocabulary.TagName;
import org.gbif.api.vocabulary.TagNamespace;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * A simple implementation of a read-only DatasetService of the registry backed by a CSV file.
 */
public class NodeServiceEmptyImpl extends EmptyNetworkEntityService<Node> implements NodeService {

  @Override
  public PagingResponse<Organization> endorsedOrganizations(@NotNull UUID nodeKey, @Nullable Pageable page) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PagingResponse<Organization> pendingEndorsements(@Nullable Pageable page) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PagingResponse<Organization> pendingEndorsements(@NotNull UUID nodeKey, @Nullable Pageable page) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PagingResponse<Installation> installations(@NotNull UUID nodeKey, @Nullable Pageable page) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Node getByCountry(Country country) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Country> listNodeCountries() {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Country> listActiveCountries() {
    throw new UnsupportedOperationException();
  }

  @Override
  public PagingResponse<Dataset> endorsedDatasets(@NotNull UUID nodeKey, @Nullable Pageable page) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<KeyTitleResult> suggest(@Nullable String s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PagingResponse<Node> listByMachineTag(String namespace, @Nullable String name, @Nullable String value, @Nullable Pageable page) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int addMachineTag(UUID targetEntityKey, TagName tagName, String value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteMachineTags(UUID targetEntityKey, TagNamespace tagNamespace) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteMachineTags(UUID targetEntityKey, TagName tagName) {
    throw new UnsupportedOperationException();
  }
}
