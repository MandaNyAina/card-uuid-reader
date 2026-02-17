# Installation de CardReader sur Android

## ✓ APK créé avec succès!

L'APK se trouve à: `/Users/mandanyaina/Projects/card-uuid-reader/CardReader.apk` (5 MiB)

## Étapes d'installation:

### Option 1: Installation via ADB (Recommandée)

1. **Préparez votre appareil Android:**
   - Allez dans Paramètres > À propos du téléphone
   - Appuyez 7 fois sur "Numéro de build" pour activer le mode développeur
   - Allez dans Paramètres > Options de développement
   - Activez "Débogage USB"
   - Connectez votre téléphone à l'ordinateur via USB

2. **Vérifiez la connexion:**
   ```bash
   adb devices
   ```
   Votre appareil doit apparaître dans la liste. Si demandé, autorisez le débogage USB sur votre téléphone.

3. **Installez l'APK:**
   ```bash
   adb install -r CardReader.apk
   ```

   OU utilisez le script automatique:
   ```bash
   ./install-apk.sh
   ```

### Option 2: Installation manuelle

1. Copiez le fichier `CardReader.apk` sur votre téléphone
2. Ouvrez le fichier APK sur votre téléphone
3. Autorisez l'installation depuis des sources inconnues si demandé
4. Installez l'application

## Utilisation de l'application:

1. **Activez le NFC:**
   - Paramètres > Connexions > NFC et paiement
   - Activez le NFC

2. **Lancez l'application CardReader**

3. **Scannez des cartes:**
   - Mode Scan Individuel (par défaut): chaque carte crée un fichier JSON séparé
   - Mode Scan Batch: scannez plusieurs cartes dans une session, puis terminez pour sauvegarder

4. **Récupérez les données scannées:**
   ```bash
   adb pull /sdcard/Android/data/com.cardreader.app/files/scans/ ./mes-scans/
   ```

## Informations de l'application:

- Package: com.cardreader.app
- Version: 1.0
- Taille: 5 MiB
- Android minimum: API 21 (Android 5.0)
- Permissions requises: NFC

## Résolution de problèmes:

### L'appareil n'est pas détecté:
```bash
adb kill-server
adb start-server
adb devices
```

### L'installation échoue:
- Vérifiez que le débogage USB est activé
- Autorisez le débogage USB sur l'appareil
- Désinstallez l'ancienne version si présente:
  ```bash
  adb uninstall com.cardreader.app
  ```

### NFC ne fonctionne pas:
- Vérifiez que votre appareil supporte le NFC:
  ```bash
  adb shell pm list features | grep nfc
  ```
- Activez le NFC dans les paramètres
- Assurez-vous que la carte est bien une MIFARE Classic

## Logs de débogage:

Pour voir les logs en temps réel:
```bash
adb logcat | grep CardReader
```
