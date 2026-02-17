Lecteur NFC pour cartes MIFARE Classic
======================================

Application Android pour scanner les cartes NFC MIFARE Classic S51K.

PREREQUIS:
- Android Studio Arctic Fox ou plus récent
- Appareil Android avec NFC (API 21+)
- Cartes MIFARE Classic

COMPILATION:
1. Ouvrir le projet dans Android Studio
2. Synchroniser Gradle
3. Connecter un appareil Android avec NFC activé
4. Compiler et installer l'APK

UTILISATION:

Mode Scan Individuel:
- Lancer l'application
- Sélectionner "Scan individuel" (mode par défaut)
- Approcher une carte du téléphone
- Chaque carte crée un fichier scan_YYYYMMDD_HHMMSS.json

Mode Scan Batch:
- Lancer l'application
- Sélectionner "Scan batch"
- Cliquer "Démarrer session"
- Scanner plusieurs cartes
- Cliquer "Terminer session"
- Toutes les cartes sont sauvegardées dans batch_YYYYMMDD_HHMMSS.json

FICHIERS GENERES:
Emplacement: /sdcard/Android/data/com.cardreader.app/files/scans/

Récupération via ADB:
adb pull /sdcard/Android/data/com.cardreader.app/files/scans/ ./export/

Sans ADB:
Utiliser un explorateur de fichiers Android pour accéder au dossier
