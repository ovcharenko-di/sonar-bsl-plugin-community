/*
 * This file is a part of SonarQube 1C (BSL) Community Plugin.
 *
 * Copyright © 2018-2019
 * Nikita Gryzlov <nixel2007@gmail.com>
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * SonarQube 1C (BSL) Community Plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * SonarQube 1C (BSL) Community Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SonarQube 1C (BSL) Community Plugin.
 */
package com.github._1c_syntax.bsl.sonar.language;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import java.util.List;

public final class BSLQualityProfile implements BuiltInQualityProfilesDefinition {

  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(
      "BSL Language Server rules",
      BSLLanguage.KEY
    );
    profile.setDefault(true);

    List<String> ruleKeys = BSLLanguageServerRuleDefinition.getActivatedRuleKeys();
    ruleKeys.forEach(ruleKey ->
      profile.activateRule(BSLLanguageServerRuleDefinition.REPOSITORY_KEY, ruleKey)
    );

    profile.done();
  }

}
