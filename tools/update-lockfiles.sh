PROJECTS=("evervault-core" "evervault-cages" "evervault-enclaves" "evervault-inputs" "sampleapplication")
for PROJECT in "${PROJECTS[@]}"
do
  ./gradlew "$PROJECT:dependencies" --write-locks
done