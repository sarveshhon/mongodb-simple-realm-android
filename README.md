# mongodb-simple-realm-android
This is a project demonstrating MongoDB Realm Database on Android App.

In this Project i have perform Operation like
- insertOne()
- findOne()
- deleteOne()

# App Link - https://drive.google.com/file/d/1TBSXp0C2MXmebbZaG-Mtyjr2kRlQyQNq/view?usp=sharing

# Instructions

- Add this to build.gradle(Project) –

      classpath "io.realm:realm-gradle-plugin:10.7.0"
    
  
- Add this to build.gradle(Module) –

      apply plugin: 'realm-android'

      realm {
          syncEnabled = true
      }
