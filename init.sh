#!/bin/bash
#################################################################
# This is an initialisation script that should be run once only
# when you first clone the project. It performs the following steps
#
# 1. Create a README
# 2. Sets up the groupIds and artifact ids
# 3. Removes this file
# 4. Initialises git
#################################################################

export existingGroupId=com\.example
export existingArtifactId=react-springboot-appengine-template

echo "Group Id?"
read groupId

echo "Artifact Id?"
read artifactId

echo "Creating README"
mv ./README-replace.md ./README.md

echo "Replacing all occurrences of $existingGroupId with $groupId in files"
find . -type f -name "pom.xml" -exec sed -i'' -e 's/'$existingGroupId'/'$groupId'/g' {} +
find . -type f -name "*.json" -exec sed -i'' -e 's/'$existingGroupId'/'$groupId'/g' {} +
find . -type f -name "*.java" -exec sed -i'' -e 's/'$existingGroupId'/'$groupId'/g' {} +
find . -type f -name "logback.xml" -exec sed -i'' -e 's/'$existingGroupId'/'$groupId'/g' {} +

echo "Replacing all occurrences of $existingArtifactId with $artifactId in files"
find . -type f -name "pom.xml" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +
find . -type f -name "*.json" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +
find . -type f -name "*.tsx" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +
find . -type f -name "*.ts" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +
find . -type f -name "*.js" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +
find . -type f -name "*.ini" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +
find . -type f -name "*.md" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +

echo "Setting up git project"
rm init.sh
rm -rf .git
git init
git add .
git commit -m "Initialised $artifactId"