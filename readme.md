# Kinde Android generator

The generator for the [Kinde Android SDK](https://github.com/kinde-oss/kinde-sdk-android).

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://makeapullrequest.com) [![Kinde Docs](https://img.shields.io/badge/Kinde-Docs-eee?style=flat-square)](https://kinde.com/docs/developer-tools) [![Kinde Community](https://img.shields.io/badge/Kinde-Community-eee?style=flat-square)](https://thekindecommunity.slack.com)

## Overview

This generator creates an SDK in Kotlin that can authenticate to Kinde using the Authorization Code grant or the Authorization Code with PKCE grant via the [OAuth 2.0 protocol](https://oauth.net/2/). It can also access the [Kinde Management API](https://kinde.com/api/docs/#kinde-management-api) using the client credentials grant.

Also, see the SDKs section in Kinde’s [contributing guidelines](https://github.com/kinde-oss/.github/blob/main/.github/CONTRIBUTING.md).

## Usage

### Requirements

You will need the following tools to be able to generate the SDK.

#### OpenJDK

Generating the SDK will require OpenJDK to be installed, instructions for which can be found [here](https://openjdk.org/install/). Please ensure that the `JAVA_HOME` environment variable is set and added to your `PATH`.

### Initial set up

1. Clone the repository to your machine:

   ```bash
   git clone https://github.com/kinde-oss/kinde-android-generator.git
   ```

2. Go into the project:

   ```bash
   cd kinde-android-generator
   ```

3. Install the OpenAPI Generator tool:

   https://openapi-generator.tech/docs/installation

### SDK generation

Run the following command to generate the SDK:

```bash
./generate.sh
```

**Note:** The API specifications should always point to Kinde's hosted version: https://kinde.com/api/kinde-mgmt-api-specs.yaml. This is set via the ` -i` option in the [OpenAPI Generator CLI](https://openapi-generator.tech/docs/usage/), for example:

```bash
openapi-generator-cli generate -i https://kinde.com/api/kinde-mgmt-api-specs.yaml
```

The SDK gets outputted to: `kinde-sdk`, which you can enter via:

```bash
cd ./kinde-sdk
```

## SDK documentation

[Android SDK](https://kinde.com/docs/developer-tools/android-sdk/)

## Development

The instructions provided in the "Usage" section above are sufficient to get you started.

## Contributing

Please refer to Kinde’s [contributing guidelines](https://github.com/kinde-oss/.github/blob/489e2ca9c3307c2b2e098a885e22f2239116394a/CONTRIBUTING.md).

## License

By contributing to Kinde, you agree that your contributions will be licensed under its MIT License.
