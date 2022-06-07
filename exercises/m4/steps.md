## Producer

### Create Constructor
1. Declare a member variable of type `Connection` in the `Producer` class
1. Update the constructor and pass it the host name, which should be localhost, and the name identifier for the producer application, this can be anything but we use "app:producer"
    - In the constructor create a connection and assign it to the member defined above

### Create a channel
1. Declare a member variable of type `Channel` in the `Producer` class
1. Add a method called `createChannel` and in this method create a `channel` and assign it to the `channel` variable

### Publish a message
1. Add a `public final static String` called `QUEUE_NAME` and give it the value "direct"
1. Add a method called `publishMessage` and in this method call `basicPublish` to publish a message and print out a message saying that the message has been published


## Consumer

### Create Constructor
1. Declare a member variable of type `Connection` in the `Producer` class
1. Update the constructor and pass it the host name, which should be localhost, and the name identifier for the producer application, this can be anything but we use "app:producer"
    - In the constructor create a connection and assign it to the member defined above

### Create a channel
1. Declare a member variable of type `Channel` in the `Producer` class
1. Add a method called `createChannel` and in this method create a `channel` and assign it to the `channel` variable

### Declare a queue
1. Add a `public final static String` called `QUEUE_NAME` and give it the value "direct"
1. Add a method called `declareQueue` and in this method declare a queue with the name `QUEUE_NAME`

### Consume a message
1. Add a method called `basicConsume`
1. Define a variable of type `DeliverCallback` this is the callback used to consume the message. This is a "functional interface" the single method of which takes two parameters
`(String consumerTag, Delivery delivery) ` Use the `delivery` parameter to get the message and print it onto the console
1. In this method call `channel.basicConsume` passing in the `DeliverCallback` you just declared

### Bringing it all Together
1. If you now run the producer and the consumer in different terminal windows then the producer should send a message to RabbitMQ and the consumer should consume that message and display it on the console, to do this
1. Change directories to `before` and run `gradle build`
    > all the tests should pass
1. In this terminal run `gradle Producer:run`
1. Create a new terminal window, in this terminal:
    1. change directory to `before`
    1. type `gradle Consumer:run`
1. You should see the message you printed out when the consumer consumes a message










Open the **Custom Test** section and take a look at the **Test Output** section for more details.