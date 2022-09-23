package webhook;

import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;

import static spark.Spark.port;
import static spark.Spark.post;

public class WebHook {
    public static void main(String[] args) {
        port(4242);
        // This is your test secret API key.
        Stripe.apiKey = "sk_test_51LgYQKCdDlvaazzQ89yjW5Iucna6bMLzJ7oumuEMcBvrVGNs4BrqFe7J3pMaZzfK2ZM1RRroBfOuGwiM5159Cjlg00sn8Y3MPN";
        // Replace this endpoint secret with your endpoint's unique secret
        // If you are testing with the CLI, find the secret by running 'stripe listen'
        // If you are using an endpoint defined with the API or dashboard, look in your webhook settings
        // at https://dashboard.stripe.com/webhooks
        String endpointSecret = "whsec_40da19b69f30628b375fdae51bec0aefd2caf43be36a2d0fb6c12df1da8e738a";

        post("/webhook", (request, response) -> {
            String payload = request.body();
            Event event = null;

            try {
                event = ApiResource.GSON.fromJson(payload, Event.class);
            } catch (JsonSyntaxException e) {
                // Invalid payload
                System.out.println("⚠️  Webhook error while parsing basic request.");
                response.status(400);
                return "";
            }
            String sigHeader = request.headers("Stripe-Signature");
            if(endpointSecret != null && sigHeader != null) {
                // Only verify the event if you have an endpoint secret defined.
                // Otherwise use the basic event deserialized with GSON.
                try {
                    event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
                } catch (SignatureVerificationException e) {
                    // Invalid signature
                    System.out.println("⚠️  Webhook error while validating signature.");
                    response.status(400);
                    return "";
                }
            }
            // Deserialize the nested object inside the event
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                // Deserialization failed, probably due to an API version mismatch.
                // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                // instructions on how to handle this case, or return an error here.
            }
            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
//                    System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                    // Then define and call a method to handle the successful payment intent.
                    // handlePaymentIntentSucceeded(paymentIntent);
                    break;
                case "checkout.session.completed":
//                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
//                    System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                    // Then define and call a method to handle the successful payment intent.
                    // handlePaymentIntentSucceeded(paymentIntent);
                    break;
                case "payment_method.attached":
                    PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                    // Then define and call a method to handle the successful attachment of a PaymentMethod.
                    // handlePaymentMethodAttached(paymentMethod);
                    break;
                default:
                    System.out.println("Unhandled event type: " + event.getType());
                    break;
            }
            response.status(200);
            return "";
        });
    }
}
