import DialogConcludeSession from '@components/main/dialog/DialogConcludeSession';
import DialogStartSession from '@components/main/dialog/DialogStartSession';
import { useEffect, useRef, useState } from 'react';
import { Button, Text } from 'react-native-paper';
import { buttonStyles } from '@styles/buttonStyles';
import { sessionService } from 'src/services/sessionService';
import { textStyles } from '@styles/textStyles';

export const ButtonSessionStarter = () => {

  /**
   * Referemces
   */
  const dialogStartSessionRef = useRef<any>();
  const dialogConcludeSessionRef = useRef<any>();

  /**
   * States
   */
  const [sessionStarted, setSessionStarted] = useState(false);

  /**
   * Functions
   */
  const showDialogStartSessionRef = () => {
    dialogStartSessionRef.current?.showDialog();
  }

  const showDialogConcludeSessionRef = () => {
    dialogConcludeSessionRef.current?.showDialog();
  }

  const showDialog = () => {
    if (sessionService.isSessionInProgress)
      showDialogConcludeSessionRef();
    else
      showDialogStartSessionRef();
  }

  /**
   * Effects
   */
  useEffect(() => {
    const subscription = sessionService.subscribeToSessionStatus((isSessionInProgress) => {
      setSessionStarted(isSessionInProgress);
    })
    return () => { subscription.unsubscribe() };
  }, []);

  /**
   * Render
   */
  return (
    <>
      <Button
        mode="elevated"
        style={sessionStarted ? buttonStyles.important : buttonStyles.highlighted}
        onPress={showDialog}
      >
        <Text style={textStyles.buttonText} variant="labelLarge">
          {sessionStarted ? 'STOP SESSION' : 'START SESSION'}
        </Text>
      </Button>
      <DialogStartSession ref={dialogStartSessionRef} />
      <DialogConcludeSession ref={dialogConcludeSessionRef} />
    </>
  );
};
