import React, { useEffect, useState } from "react";
import { StyleSheet, View } from "react-native";

import { settingsStyles } from "../../../styles/settings-styles";
import { SettingsStatus } from "../../../const/settings";

import AccodionItem from "../accordion-item.component";
import ChoiceBackend from "./choice-backend.component";
import ConfirmButton from "../confirm-button.component";
import * as backendChoiceService from "../../../services/backend-choice.service";

const CONFIRM_BUTTON_TEXT = "Confirm Choice";

const SettingsMenuItemBackend = () => {

  /**
   * States
   */
  const [stateHeaderSummary, setstateHeaderSummary] = useState("");
  const [stateSettingStatus, setStateSettingStatus] = useState(SettingsStatus.UNSET);

  /**
   * Functions
   */

  /**
   * Render
   */
  return (
    <AccodionItem
      title="Backend Type"
      summaryText={stateHeaderSummary}
      settingStatus={stateSettingStatus}
    >
      <ChoiceBackend
        style={settingsStyles.sectionSpacing}
      />
      <View style={styles.spacing}>
        <ConfirmButton
          isActive={false}
          text={CONFIRM_BUTTON_TEXT}
          handleButtonPressedParentFunction={() => { }}
        />
      </View>
    </AccodionItem>
  );
};

/**
 * Style Sheet
 */
const styles = StyleSheet.create({
  spacing: {
    flexDirection: "column",
    alignItems: "flex-end",
    marginTop: 20,
  },
});

export default SettingsMenuItemBackend;