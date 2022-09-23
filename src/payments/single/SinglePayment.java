package payments.single;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.nio.file.Paths;

import static spark.Spark.*;

public class SinglePayment {

  public static void main(String[] args) {
    port(4242);

    // This is a public sample test API key.
    // Don’t submit any personally identifiable information in requests made with this key.
    // Sign in to see your own test API key embedded in code samples.
    Stripe.apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";

    staticFiles.externalLocation(
        Paths.get("public").toAbsolutePath().toString());

    post("/create-checkout-session", (request, response) -> {
        String YOUR_DOMAIN = "http://localhost:3000";
        SessionCreateParams params =
          SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(YOUR_DOMAIN + "?success=true")
            .setCancelUrl(YOUR_DOMAIN + "?canceled=true")
            .addLineItem(
              SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setName("Stubborn Attachments")

                .setAmount(2000L)
                .setCurrency("USD")
                //Provide the exact Price ID (for example, pr_1234) of the product you want to sell
//              .setPrice("price_1LkudPCdDlvaazzQeVO650uO")
                .build())
            .build();
      Session session = Session.create(params);

      response.redirect(session.getUrl(), 303);
      return "";
    });
  }
}
