#!/bin/sh
# wait-for-mysql.sh

set -e

host="$1"
shift
cmd="$@"

echo "Waiting for MySQL at $host..."

until mysql -h "$host" -u"$MYSQL_USERNAME" -p"$MYSQL_PASSWORD" -e "SELECT 1" >/dev/null 2>&1; do
  echo "MySQL not ready yet..."
  sleep 2
done

echo "MySQL is up - starting application"
exec $cmd
