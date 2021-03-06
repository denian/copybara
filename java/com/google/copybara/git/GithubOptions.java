/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.copybara.git;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.base.Preconditions;
import com.google.copybara.GeneralOptions;
import com.google.copybara.Option;
import com.google.copybara.RepoException;
import com.google.copybara.git.github_api.GitHubApiTransportImpl;
import com.google.copybara.git.github_api.GithubApi;
import java.util.function.Supplier;

/**
 * Options related to GitHub
 */
public class GithubOptions implements Option {

  private final Supplier<GeneralOptions> generalOptionsSupplier;
  private final GitOptions gitOptions;

  public GithubOptions(Supplier<GeneralOptions> generalOptionsSupplier, GitOptions gitOptions) {
    this.generalOptionsSupplier = Preconditions.checkNotNull(generalOptionsSupplier);
    this.gitOptions = Preconditions.checkNotNull(gitOptions);
  }

  public GithubApi getApi() throws RepoException {
    GitRepository repo = gitOptions.cachedBareRepoForUrl("just_for_github_api");
    return new GithubApi(new GitHubApiTransportImpl(repo, getHttpTransport()),
        generalOptionsSupplier.get().profiler());
  }

  protected HttpTransport getHttpTransport() {
    return new NetHttpTransport();
  }
}
