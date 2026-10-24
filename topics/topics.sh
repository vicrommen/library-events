#!/bin/sh
set -eu

CONFIG=/topics/configuration.yaml
BOOTSTRAP_SERVER=${BOOTSTRAP_SERVER:-kafka1:19092}
KAFKA_TOPICS=/opt/kafka/bin/kafka-topics.sh
MAX_ATTEMPTS=30

attempt=1
until "$KAFKA_TOPICS" --bootstrap-server "$BOOTSTRAP_SERVER" --list > /dev/null 2>&1; do
  if [ "$attempt" -ge "$MAX_ATTEMPTS" ]; then
    echo "ERROR: Kafka not available at $BOOTSTRAP_SERVER after $MAX_ATTEMPTS attempts"
    exit 1
  fi
  echo "Waiting for Kafka at $BOOTSTRAP_SERVER ($attempt/$MAX_ATTEMPTS)..."
  attempt=$((attempt + 1))
  sleep 2
done

missing=$(yq '[.topics[] | select(.name == null or .name == "")] | length' "$CONFIG")
if [ "$missing" -ne 0 ]; then
  echo "ERROR: $missing topic(s) in $CONFIG have no name"
  exit 1
fi

count=$(yq '.topics | length' "$CONFIG")
i=0
while [ "$i" -lt "$count" ]; do
  name=$(yq ".topics[$i].name" "$CONFIG")
  partitions=$(yq ".topics[$i].partitions // \"\"" "$CONFIG")
  replication_factor=$(yq ".topics[$i].replicationFactor // \"\"" "$CONFIG")

  set -- --create --if-not-exists --topic "$name"
  [ -n "$partitions" ] && set -- "$@" --partitions "$partitions"
  [ -n "$replication_factor" ] && set -- "$@" --replication-factor "$replication_factor"
  for config in $(yq ".topics[$i].configs // {} | to_entries | .[] | .key + \"=\" + (.value | tostring)" "$CONFIG"); do
    set -- "$@" --config "$config"
  done

  "$KAFKA_TOPICS" --bootstrap-server "$BOOTSTRAP_SERVER" "$@"
  echo "Topic '$name' ensured"
  i=$((i + 1))
done
