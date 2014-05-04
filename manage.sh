#!/bin/bash

#export JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26/jre 
export JAVA_HOME=/usr/lib/jvm/java-7-oracle/jre 
export SDK_HOME=/home/benito/opt/android-sdk-linux/platform-tools/

case "$1" in
build)
  if [[ $2 == "release" ]]; then BUILDMODE=release
  else BUILDMODE=debug;  fi

  ant $BUILDMODE || (echo "FAILED!"; exit)

  $SDK_HOME/adb uninstall de.benibela.xkcd1363 || (echo "FAILED!"; exit)
  $SDK_HOME/adb install bin/xkcd1363-$BUILDMODE.apk || (echo "FAILED!"; exit)
;;


install)
  if [[ $2 == "release" ]]; then BUILDMODE=release
  else BUILDMODE=debug;  fi

  cd android
  $SDK_HOME/adb uninstall de.benibela.xkcd1363 || (echo "FAILED!"; exit)
  $SDK_HOME/adb install bin/xkcd1363-$BUILDMODE.apk || (echo "FAILED!"; exit)
  
;;

clean)
  ant clean
;;
  
esac


