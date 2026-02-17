#!/bin/bash

echo "========================================="
echo "Installation CardReader sur Android"
echo "========================================="
echo ""

adb devices -l

if [ $? -ne 0 ]; then
    echo "Erreur: ADB n'est pas disponible"
    exit 1
fi

device_count=$(adb devices | grep -w "device" | wc -l)

if [ $device_count -eq 0 ]; then
    echo ""
    echo "Aucun appareil détecté."
    echo ""
    echo "Veuillez:"
    echo "1. Connecter votre téléphone Android via USB"
    echo "2. Activer le débogage USB dans les paramètres développeur"
    echo "3. Autoriser le débogage USB sur votre téléphone"
    echo ""
    echo "Puis relancez ce script: ./quick-install.sh"
    exit 1
fi

echo ""
echo "Appareil détecté! Installation en cours..."
echo ""

adb install -r CardReader.apk

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "✓ Installation réussie!"
    echo "========================================="
    echo ""
    echo "L'application CardReader est installée."
    echo ""
    echo "Prochaines étapes:"
    echo "1. Activez le NFC sur votre téléphone"
    echo "   (Paramètres > Connexions > NFC)"
    echo ""
    echo "2. Lancez l'application CardReader"
    echo ""
    echo "3. Scannez vos cartes MIFARE Classic"
    echo ""
    echo "Pour récupérer les scans:"
    echo "  adb pull /sdcard/Android/data/com.cardreader.app/files/scans/ ./mes-scans/"
    echo ""
else
    echo ""
    echo "========================================="
    echo "✗ Erreur lors de l'installation"
    echo "========================================="
    echo ""
    echo "Essayez de désinstaller l'ancienne version:"
    echo "  adb uninstall com.cardreader.app"
    echo ""
    echo "Puis relancez: ./quick-install.sh"
    echo ""
fi
