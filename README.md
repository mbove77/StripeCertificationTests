# Accept a Payment with Stripe Checkout

Stripe Checkout is the fastest way to get started with payments. Included are some basic build and run scripts you can use to start up the application.

## Running the sample

1. Build the server

~~~
mvn package
~~~

2. Run the server

~~~
java -cp target/stripe-payment-1.0.0-SNAPSHOT-jar-with-dependencies.jar payments.single.SinglePayment
~~~

3. Build the payments.single.client app

~~~
npm install
~~~

4. Run the payments.single.client app

~~~
npm start
~~~

5. Go to [http://localhost:3000/checkout](http://localhost:3000/checkout)
