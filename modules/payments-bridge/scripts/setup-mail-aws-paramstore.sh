#!/bin/bash
#
# This scripts creates AWS System Manager Parameter Store parameters for Payments Bridge e-mail
# notifications.
#
# Usage:
#   ./setup-mail-aws-paramstore.sh --profile <AWS PROFILE> --region <AWS REGION> [OTHER OPTIONS...]
#

set -eo pipefail

read -sp 'Password: ' password

echo
echo "Creating /services/pb/mail/host..."
aws "$@" ssm put-parameter --name /services/pb/mail/host --value smtp.gmail.com --type String --overwrite
echo "Creating /services/pb/mail/port..."
aws "$@" ssm put-parameter --name /services/pb/mail/port --value 587 --type String --overwrite
echo "Creating /services/pb/mail/username..."
aws "$@" ssm put-parameter --name /services/pb/mail/username --value noreply@silenteight.com --type String --overwrite
echo "Creating /services/pb/mail/password..."
aws "$@" ssm put-parameter --name /services/pb/mail/password --value "${password}" --type SecureString --overwrite
