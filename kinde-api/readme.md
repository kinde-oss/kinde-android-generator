# Howto to generate SDK

1. Setup variables in `env.properties` file:
    - `yaml_path` - path to openapi specification file
    - `sdk_dir` - output path for generated sdk


            yaml_path=../kinde-mgmt-api-specs.yaml
            sdk_dir=../kinde-sdk/


2. Navigate to `root` directory and run `generate.sh`



**Troubleshooting**

- make sure paths in `env.properties` are correct
- in case of permission denied errors plesae check that `gradlew` files have executable permission
