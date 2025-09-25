set -e

echo "signingKey=${GPG_KEY_FILE}" >> ${1}/local.properties
echo "signingPassword=${GPG_KEY_PASSWORD}" >> ${1}/local.properties
echo "ossrhUsername=${SONATYPE_USERNAME}" >> ${1}/local.properties
echo "ossrhPassword=${SONATYPE_PASSWORD}" >> ${1}/local.properties
