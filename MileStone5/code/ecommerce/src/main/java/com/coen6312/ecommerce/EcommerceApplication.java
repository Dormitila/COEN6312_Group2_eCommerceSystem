package com.coen6312.ecommerce;

import com.coen6312.ecommerce.controller.AdminController;
import com.coen6312.ecommerce.controller.BuyerController;
import com.coen6312.ecommerce.controller.CSRController;
import com.coen6312.ecommerce.controller.SellerController;
import com.coen6312.ecommerce.entity.CustomerServiceRepresentative;
import com.coen6312.ecommerce.service.EcommerceSystemService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}

@Component
class EcommerceCommandLineRunner implements CommandLineRunner {

	private final EcommerceSystemService systemService;
	private final BuyerController buyerController;
	private final SellerController sellerController;
	private final AdminController adminController;
	private final CSRController csrController;

	public EcommerceCommandLineRunner(EcommerceSystemService systemService, BuyerController buyerController,
									  SellerController sellerController, AdminController adminController, CSRController csrController) {
		this.systemService = systemService;
		this.buyerController = buyerController;
		this.sellerController = sellerController;
		this.adminController = adminController;
		this.csrController = csrController;
	}

	@Override
	public void run(String... args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while(true) {
			System.out.println("Select Role:");
			System.out.println("1. Buyer");
			System.out.println("2. Seller");
			System.out.println("3. Admin");
			System.out.println("4. Customer Service Representative");
			System.out.println("5. Exit");
			String role = reader.readLine();

			switch (role) {
				case "1":
					handleBuyerOptions(reader);
					break;
				case "2":
					handleSellerOptions(reader);
					break;
				case "3":
					handleAdminOptions(reader);
					break;
				case "4":
					handleCSROptions(reader);
					break;
				case "5":
					System.out.println("Exiting.");
					return;
				default:
					System.out.println("Invalid selection. Please enter a valid number.");
					break;
			}
		}
	}

	private void handleBuyerOptions(BufferedReader reader) throws Exception {
		while (true) {
			System.out.println("\nBuyer Options:");
			System.out.println("1. View All Items");
			System.out.println("2. View Shoes by Filter"); // Placeholder for future implementation
			System.out.println("3. View Shoe Details");
			System.out.println("4. Add Shoes to Cart");
			System.out.println("5. Remove Shoes from Cart");
			System.out.println("6. View My Cart");
			System.out.println("7. View My Orders");
			System.out.println("8. View Specific Order");
			System.out.println("9. Checkout Cart");
			System.out.println("10. Raise Complaint");
			System.out.println("11. Write Feedback");
			System.out.println("12. View My Complaints");
			System.out.println("13. Manage Addresses");
			System.out.println("14. Return to Main Menu");
			String option = reader.readLine();

			switch (option) {
				case "1":
					buyerController.displayAllItems(reader);
					break;
				case "2":
					buyerController.searchAndDisplayShoesByFilter(reader);
					break;
				case "3":
					buyerController.displayProductDetails(reader);
					break;
				case "4":
					buyerController.addShoesToCart(reader);
					break;
				case "5":
					buyerController.removeShoesFromCart(reader);
					break;
				case "6":
					buyerController.viewMyCart(reader);
					break;
				case "7":
					buyerController.viewMyOrders(reader);
					break;
				case "8":
					buyerController.viewOrderByID(reader);
					break;
				case "9":
					buyerController.cartCheckOut(reader);
					break;
				case "10":
					buyerController.raiseComplaint(reader);
					break;
				case "11":
					buyerController.writeFeedback(reader);
					break;
				case "12":
					buyerController.viewMyComplaints(reader);
					break;
				case "13":
					buyerController.manageAddresses(reader);
					break;
				case "14":
					return; // Returns to the main menu
				default:
					System.out.println("Invalid option. Please enter a valid number.");
					break;
			}
		}
	}

	private void handleSellerOptions(BufferedReader reader) throws Exception {
		while (true) {
			System.out.println("Seller Options:");
			System.out.println("1. Add New Product");
			System.out.println("2. Check My Stock");
			System.out.println("3. Update Product By Id");
			System.out.println("4. Remove Product By Id");
			System.out.println("5. View Complaints From Buyers");
			System.out.println("6. View Warnings From Admin");
			System.out.println("7. View Out of Stock Messages");
			System.out.println("8. Exit");

			String option = reader.readLine();
			switch (option) {
				case "1":
					sellerController.addNewProduct(reader);
					break;
				case "2":
					sellerController.checkMyStock(reader);
					break;
				case "3":
					sellerController.updateProductById(reader);
					break;
				case "4":
					sellerController.removeProductById(reader);
					break;
				case "5":
					sellerController.viewComplaintsFromBuyer(reader);
					break;
				case "6":
					sellerController.viewWarningsFromAdmin(reader);
					break;
				case "7":
					sellerController.viewOutOfStockMessages(reader);
					break;
				case "8":
					System.out.println("Returning to main menu.");
					return; // Returns to the main menu
				default:
					System.out.println("Invalid option. Please enter a valid number.");
					break;
			}
		}
	}

	private void handleAdminOptions(BufferedReader reader) throws Exception {
		while (true) {
			System.out.println("\nAdmin Options:");
			System.out.println("1. View All Products");
			System.out.println("2. Block Seller");
			System.out.println("3. View notifications from Customer Service Representatives");
			System.out.println("4. Send a Warning to Seller");
			System.out.println("5. Exit");

			String option = reader.readLine();
			switch (option) {
				case "1":
					adminController.viewAllProducts();
					break;
				case "2":
					adminController.blockSeller(reader);
					break;
				case "3":
					adminController.sendWarnings(reader);
					break;
				case "4":
					adminController.viewNotifications(reader);
					break;
				case "5":
					System.out.println("Returning to main menu.");
					return;
				default:
					System.out.println("Invalid option. Please enter a valid number.");
					break;
			}
		}
	}

	private void handleCSROptions(BufferedReader reader) throws Exception {
		while (true) {
			System.out.println("\nCustomer Service Representative Options:");
			System.out.println("1. View Complaints");
			System.out.println("2. Escalate Complaints");
			System.out.println("3. Resolve Issues");
			System.out.println("4. Exit");

			String option = reader.readLine();
			switch (option) {
				case "1":
					csrController.viewComplaints(reader);
					break;
				case "2":
					csrController.escalateComplaints(reader);
					break;
				case "3":
					csrController.resolveIssues(reader);
					break;
				case "4":
					System.out.println("Returning to main menu.");
					return;
				default:
					System.out.println("Invalid option. Please enter a valid number.");
					break;
			}
		}
	}
}