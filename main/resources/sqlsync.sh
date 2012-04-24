#!/bin/bash

SCRIPT_HOME_DIR="/home/sqlsync"
DATABASE="database"
SQLSYNC="SqlSync"
WEB_DIR="/var/www/sqlsync"
SQLSYNC_RUN="target/SqlSync.jar sqlsync.json"
CREATE_SQLSYNC_INDEX="/home/sqlsync/create_sqlsync_index.sh"

cd ${SCRIPT_HOME_DIR}"/"${DATABASE}
svn up .

cd ${SCRIPT_HOME_DIR}"/"${SQLSYNC}
/usr/local/java/latest/bin/java -jar ${SQLSYNC_RUN}

if [ `find . -name "*.html" | wc -l` -gt 0 ]; then
  mv -f *.html ${WEB_DIR}
fi

${CREATE_SQLSYNC_INDEX}
