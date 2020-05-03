/*
 * This file is a part of SonarQube 1C (BSL) Community Plugin.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com>
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
package com.github._1c_syntax.bsl.sonar.acc;

import com.github._1c_syntax.bsl.sonar.language.BSLLanguage;
import com.github._1c_syntax.bsl.sonar.language.BSLLanguageServerRuleDefinition;
import org.jetbrains.annotations.NotNull;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import java.util.List;

public class ACCQualityProfile implements BuiltInQualityProfilesDefinition {

  private final List<String> rulesBSL;
  private final ACCRulesFile rulesFile;


  public ACCQualityProfile() {
    this.rulesBSL = BSLLanguageServerRuleDefinition.getActivatedRuleKeys();
    this.rulesFile = ACCRuleDefinition.getRulesFile();
  }

  @Override
  public void define(@NotNull Context context) {

    if (rulesFile == null) {
      return;
    }

    addACCFullCheckProfile(context);
    addACC1CCertifiedProfile(context);
    addFullCheckProfile(context);

  }

  private void addACCFullCheckProfile(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(
      "ACC - full check",
      BSLLanguage.KEY
    );
    rulesFile.getRules()
      .stream()
      .filter(ACCRulesFile.ACCRule::isActive)
      .map(ACCRulesFile.ACCRule::getCode)
      .forEach(key -> profile.activateRule(ACCRuleDefinition.REPOSITORY_KEY, key));
    profile.done();
  }

  private void addACC1CCertifiedProfile(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(
      "ACC - 1C:Certified",
      BSLLanguage.KEY
    );
    rulesFile.getRules()
      .stream()
      .filter(ACCRulesFile.ACCRule::isNeedForCertificate)
      .map(ACCRulesFile.ACCRule::getCode)
      .forEach(key -> profile.activateRule(ACCRuleDefinition.REPOSITORY_KEY, key));
    profile.done();
  }

  private void addFullCheckProfile(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(
      "BSL - all rules",
      BSLLanguage.KEY
    );
    rulesFile.getRules()
      .stream()
      .filter(ACCRulesFile.ACCRule::isActive)
      .map(ACCRulesFile.ACCRule::getCode)
      .forEach(key -> profile.activateRule(ACCRuleDefinition.REPOSITORY_KEY, key));
    rulesBSL
      .forEach(key -> profile.activateRule(BSLLanguageServerRuleDefinition.REPOSITORY_KEY, key));
    profile.done();
  }

}
