// src/native/CustomTTS.ts
import { NativeModules } from 'react-native';
const { CustomTTS } = NativeModules;

export default {
  init: () => CustomTTS.initTTS(),
  speakText: (text: string, voice: string, speed: number) =>
    CustomTTS.speakText(text, voice, speed),
  stop: () => CustomTTS.stop(),
};
