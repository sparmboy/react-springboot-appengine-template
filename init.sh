#!/bin/bash
# find_and_replace.sh

export existingGroupId=com\.example
export existingArtifactId=react-springboot-appengine-template

echo "Group Id?"
read groupId

echo "Artifact Id?"
read artifactId

echo "Replacing all occurences of $existingGroupId with $groupId in files"
find . -type f -name "pom.xml" -exec sed -i'' -e 's/'$existingGroupId'/'$groupId'/g' {} +
find . -type f -name "*.json" -exec sed -i'' -e 's/'$existingGroupId'/'$groupId'/g' {} +

echo "Replacing all occurences of $existingArtifactId with $artifactId in files"
find . -type f -name "pom.xml" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +
find . -type f -name "*.json" -exec sed -i'' -e 's/'$existingArtifactId'/'$artifactId'/g' {} +