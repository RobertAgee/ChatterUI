import React, { useEffect, useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet, Alert } from 'react-native';
import { NativeModules } from 'react-native';

const { CustomTTS } = NativeModules;

export default function TTSExample() {
  const [text, setText] = useState("Hello! This is a test.");
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    CustomTTS.initTTS()
      .then(() => {
        setIsReady(true);
        console.log("TTS initialized");
      })
      .catch((e: any) => {
        console.error("TTS init failed:", e);
        Alert.alert("Error", "Failed to initialize TTS.");
      });
  }, []);

  const handleSpeak = () => {
    if (!isReady) return;

    CustomTTS.speakText(text, "af", 1.0)
      .then(() => console.log("Speech done"))
      .catch((e: any) => {
        console.error("TTS error:", e);
        Alert.alert("Error", "Failed to speak.");
      });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.label}>Enter text to speak:</Text>
      <TextInput
        style={styles.input}
        value={text}
        onChangeText={setText}
        placeholder="Type something"
      />
      <Button title="Speak" onPress={handleSpeak} disabled={!isReady} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    justifyContent: 'center',
  },
  label: {
    fontSize: 18,
    marginBottom: 12,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    padding: 12,
    fontSize: 16,
    marginBottom: 20,
    borderRadius: 8,
  },
});
