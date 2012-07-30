#!/bin/bash

WEB_DIR="/var/www/sqlsync"
WEB_DIR_INDEX=${WEB_DIR}"/index.html"
SQL_SYNC_URL="http://sqlsync.cleverdolphin.se"

echo '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"><html><head><title>SQLSYNC</title></head><style> body { font-family: "Lucida Grande", Verdana, Arial, Helvetica, sans-serif;' > ${WEB_DIR_INDEX}
echo 'font-size: 14px; text-align: center;} h1 {font-size: 20px; font-weight: normal;}</style>' >> ${WEB_DIR_INDEX}
echo '<body><h1>SQLSYNC</h1>' >> ${WEB_DIR_INDEX}
echo '<br>' >> ${WEB_DIR_INDEX}

for FILE in `ls ${WEB_DIR}`; do
 if [ ${FILE} != 'index.html' ]; then
   LINK='<a href="'${SQL_SYNC_URL}"/"${FILE}'">'${FILE}'</a>'
   echo ${LINK} >> ${WEB_DIR_INDEX}
   echo '<br>' >> ${WEB_DIR_INDEX}
 fi
done

echo '</body>' >> ${WEB_DIR_INDEX}
