#!/bin/bash

echo "Construction de l'APK CardReader..."

if [ ! -d "$ANDROID_HOME" ]; then
    echo "ERREUR: ANDROID_HOME n'est pas défini"
    echo "Veuillez installer Android Studio et définir ANDROID_HOME"
    exit 1
fi

if [ ! -f "gradlew" ]; then
    echo "Téléchargement de Gradle Wrapper..."
    gradle wrapper
fi

chmod +x gradlew

echo "Nettoyage..."
./gradlew clean

echo "Construction de l'APK debug..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "APK créé avec succès!"
    echo "Emplacement: app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "Pour installer sur un appareil connecté:"
    echo "adb install -r app/build/outputs/apk/debug/app-debug.apk"
else
    echo "Erreur lors de la construction"
    exit 1
fi
