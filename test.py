import uuid
import random
from datetime import datetime, timedelta

user_id = "c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786"
category_ids = [
    "4b1e0ad3-0d8a-4f8a-b93e-cb2e2aee5b0b","99a013c5-3346-44e7-9cb9-4b3e5e00271b","a417a045-686e-4ab6-bbf1-1e5b33d2be93",
    "3d405321-87be-4e89-b57e-fbfa9579f15d","db73bb60-f9d8-40c3-84a5-4f1bdb62bc27","e39cf8e8-b99b-4650-928d-cf89f74e4203",
    "4d414573-3b45-4b22-bb4c-01d348378b5d","80f246c4-8be8-40ea-aad7-0f155c815a64","ac6a9441-2d67-4416-90aa-3b9ee6e5e144",
    "ff41c66e-5ff4-4c0d-b3a7-334b8c4271d5","260b5fc8-00f4-46a6-9ecf-986e37c0d7e0","b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2",
    "a285d771-01d2-4055-9b42-cc54f9a4b2f1","129e13f2-637e-4dc6-bcf7-6e7dbebc13b9","f99e6af1-2b81-4915-a701-3fbc378e1e7b",
    "9c0f6e10-3cd1-4940-b2ee-419f10f7fd9c","b6ef6b15-c2e3-4fd5-bb6a-35bbcbc13e51","00a7e3e8-e042-40b9-9a25-3a6eb3fd3097",
    "4a2a01db-ec30-4b34-97a1-8b2f0556a222","c79a9c62-65c5-4c47-86f1-8a51b8eabdbb"
]
account_ids = [
    "a1e1b1d4-30c2-4313-bde4-215c7be04715",
    "b2f2c2e5-41d3-5424-cfe5-326d8cf15826",
    "c3a3d3f6-52e4-6535-dfe6-437e9df26937",
    "d4b4e407-63f5-7646-efe7-548f0ef37a48",
    "e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c"
]

types = ["EXPENSE", "INCOME", "TRANSFER"]
notes = [
    # Daily expenses
    "Lunch", "Taxi", "Bus fare", "Groceries", "Movie night", "Coffee", "Snacks", "Dinner with friends",
    "Supermarket", "Bakery", "Fast food", "Street food", "Petrol", "Gas station", "Parking fee", "Toll payment",
    "Laundry", "Dry cleaning", "Convenience store", "Pharmacy", "Medicine", "Doctor visit", "Hospital bill",
    "Dentist appointment", "Gym membership", "Yoga class", "Haircut", "Salon", "Beauty products",
    "Personal hygiene", "Shaving kit", "Shampoo", "Toothpaste",

    # Monthly/recurring
    "Salary", "Freelance payment", "Part-time job", "Bonus", "Tax refund", "Stock dividend", "Rental income",
    "Child allowance", "Interest", "Reimbursement", "Pension", "Allowance", "Commission", "Scholarship",

    # Bills & utilities
    "Electric bill", "Water bill", "Internet", "Mobile recharge", "Cable TV", "Streaming subscription",
    "Spotify subscription", "Netflix payment", "Amazon Prime", "Cloud storage", "Magazine subscription",

    # Home & family
    "Rent payment", "Utilities", "Home repair", "Plumber service", "Electrician service", "Furniture",
    "Appliance purchase", "Vacuum cleaner", "Microwave", "Crockery", "Curtains", "Carpet", "Garden supplies",
    "Baby sitter", "Childcare", "School fees", "Stationery", "Textbooks", "School supplies", "Toys",
    "Board games", "Kids clothing",

    # Shopping
    "Online shopping", "Electronics", "Mobile phone", "Laptop", "Tablet", "Wearables", "Clothing",
    "Shoes", "Winter jacket", "Summer dress", "Sunglasses", "Backpack", "Handbag", "Jewelry", "Watch",
    "Gift for friend", "Birthday present", "Wedding gift",

    # Entertainment & travel
    "Theater ticket", "Concert", "Event", "Sports match", "Theme park", "Hotel booking", "Flight ticket",
    "Train ticket", "Travel insurance", "Souvenirs", "Tour package", "Car rental", "Airport shuttle",
    "Museum entry", "Excursion", "Boat ride",

    # Food & drinks
    "Groceries", "Market", "Farmers market", "Fish market", "Bakery", "Butcher", "Fruit store", "Wine",
    "Beer", "Cocktail", "Restaurant", "Takeout", "Delivery", "Pizza night", "Burger joint", "Ice cream",
    "Chocolate", "Birthday cake",

    # Health & insurance
    "Health insurance", "Life insurance", "Car insurance", "Home insurance", "Prescription", "Vitamins",
    "Clinic", "Eye checkup", "Contact lenses", "Therapist", "Chiropractor",

    # Transfers & misc
    "Cash withdrawal", "ATM fee", "Bank transfer", "Wire transfer", "Card payment", "Cheque deposit",
    "Account top-up", "Savings transfer", "Investment deposit", "Cryptocurrency purchase", "Loan repayment",
    "Charity donation", "Church offering", "Political donation", "Crowdfunding", "Fundraiser", "Tip",
    "Service fee", "Penalty fee", "Late payment fee", "Overdraft fee",

    # Other
    "Uncategorized", "Miscellaneous", "Refund", "Partial refund", "Lost wallet", "Pet care", "Vet bill",
    "Adoption fee", "Pet food", "Pet toys"
]

def random_date():
    now = datetime.now()
    days_ago = random.randint(0, 89)
    rand_date = now - timedelta(days=days_ago, hours=random.randint(0,23), minutes=random.randint(0,59))
    return rand_date.strftime("%Y-%m-%d %H:%M:%S")

print("INSERT INTO records (id,amount,user_id,payment_time,category_id,type,note,currency,withdrawal_account_id,reciving_account_id) VALUES")
for i in range(2000):
    rec_id = str(uuid.uuid4())
    amount = round(random.uniform(5, 1000), 2)
    category_id = random.choice(category_ids)
    record_type = random.choice(types)
    note = random.choice(notes)
    currency = "USD"
    withdrawal_account_id = random.choice(account_ids)
    reciving_account_id = random.choice(account_ids)
    # For EXPENSE, only withdrawal; for INCOME only receiving; for TRANSFER, both and cannot be same
    if record_type == "EXPENSE":
        reciving_account_id = "NULL"
    elif record_type == "INCOME":
        withdrawal_account_id = "NULL"
    elif record_type == "TRANSFER":
        while reciving_account_id == withdrawal_account_id:
            reciving_account_id = random.choice(account_ids)
    # Print record
    print(f"('{rec_id}',{amount},'{user_id}','{random_date()}','{category_id}','{record_type}','{note}','{currency}',"
          f"{'NULL' if withdrawal_account_id=='NULL' else f'\'{withdrawal_account_id}\''},"
          f"{'NULL' if reciving_account_id=='NULL' else f'\'{reciving_account_id}\''})"
          + ("," if i < 1999 else ";"))
