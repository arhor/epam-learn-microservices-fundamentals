echo $(awslocal sqs create-queue --queue-name resource-created-events)
echo $(awslocal sqs create-queue --queue-name resource-deleted-events)
echo $(awslocal sqs create-queue --queue-name resource-handled-events)
