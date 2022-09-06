cd ./kinde-api

PROP_KEY="sdk_dir"
PROPERTY_FILE=env.properties
PROP_VALUE=`cat $PROPERTY_FILE | grep "$PROP_KEY" | cut -d'=' -f2`


rm -r $PROP_VALUE/src/main/kotlin/au/kinde/sdk/api
./gradlew openApiGenerate

rm $PROP_VALUE/settings.gradle