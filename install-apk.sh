#!/bin/bash

echo "Vérification de la connexion ADB..."
adb devices

echo ""
echo "Si votre appareil est listé ci-dessus, appuyez sur Entrée pour continuer..."
read

echo "Installation de l'APK CardReader..."
adb install -r app/build/outputs/apk/debug/app-debug.apk

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Installation réussie!"
    echo ""
    echo "L'application 'CardReader' est maintenant installée sur votre appareil."
    echo ""
    echo "Pour utiliser l'application:"
    echo "1. Activez le NFC sur votre téléphone (Paramètres > Connexions > NFC)"
    echo "2. Lancez l'application CardReader"
    echo "3. Approchez une carte MIFARE Classic"
    echo ""
    echo "Les scans seront sauvegardés dans:"
    echo "/sdcard/Android/data/com.cardreader.app/files/scans/"
    echo ""
    echo "Pour récupérer les scans:"
    echo "adb pull /sdcard/Android/data/com.cardreader.app/files/scans/ ./mes-scans/"
else
    echo ""
    echo "✗ Erreur lors de l'installation"
    echo "Vérifiez que:"
    echo "- Le débogage USB est activé"
    echo "- Vous avez autorisé le débogage USB sur votre téléphone"
    echo "- L'appareil est bien détecté par 'adb devices'"
fi
