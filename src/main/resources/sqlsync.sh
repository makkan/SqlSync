#!/bin/bash

SCRIPT_HOME_DIR="/home/sqlsync"
DATABASE_TRUNK="database_trunk"
DATABASE_PFR="database_pfr"
SQLSYNC="SqlSync"
WEB_DIR="/var/www/sqlsync"
SQLSYNC_RUN="target/SqlSync.jar $1"
CREATE_SQLSYNC_INDEX="/home/sqlsync/create_sqlsync_index.sh"

cd ${SCRIPT_HOME_DIR}"/"${DATABASE_TRUNK}
svn up .

cd ${SCRIPT_HOME_DIR}"/"${DATABASE_PFR}
svn up .

cd ${SCRIPT_HOME_DIR}"/"${SQLSYNC}
/usr/local/java/latest/bin/java -jar ${SQLSYNC_RUN}

if [ `find . -name "*.html" | wc -l` -gt 0 ]; then
  mv -f *.html ${WEB_DIR}
fi

${CREATE_SQLSYNC_INDEX}
