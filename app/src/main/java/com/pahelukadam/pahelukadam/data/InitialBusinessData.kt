package com.pahelukadam.pahelukadam.data

import com.pahelukadam.pahelukadam.model.Adminbusinessidea

object InitialBusinessData {

    val businessIdeas = listOf(

        Adminbusinessidea(
            businessName = "Casual Dining Restaurant (Regional Food)",
            description = "This business is a full-service restaurant ...",
            budget_range = "500000 - 700000",
            category_name = "Food & Beverage",
            rawMaterials = listOf(
                mapOf("title" to "Kitchen Setup (Stoves, ovens, utensils, ventilation)", "price" to 200000),
                mapOf("title" to "Restaurant Interior & Furniture", "price" to 150000),
                mapOf("title" to "Rent Deposit & Advance", "price" to 100000),
                mapOf("title" to "Initial Inventory (Groceries, spices, meats, vegetables)", "price" to 75000),
                mapOf("title" to "Licenses & Permits (FSSAI, Health, Fire, GST)", "price" to 50000),
                mapOf("title" to "POS System & Initial Marketing", "price" to 25000)
            )
        ),
        Adminbusinessidea(
            businessName = "Cold Storage",
            description = "A cold storage facility offers temperature-controlled warehousing services ...",
            budget_range = "700000 - 1000000",
            category_name = "Logistics & Storage",
            rawMaterials = listOf(
                mapOf("title" to "Prefabricated Cold Room/Chamber (Small size)", "price" to 500000),
                mapOf("title" to "Land Lease/Rental Advance", "price" to 100000),
                mapOf("title" to "Power Backup (Generator Set)", "price" to 150000),
                mapOf("title" to "Material Handling Equipment (Pallet jacks, crates)", "price" to 50000),
                mapOf("title" to "Licenses & Insurance", "price" to 50000),
                mapOf("title" to "Working Capital", "price" to 50000)
            )
        ),
        Adminbusinessidea(
            businessName = "Ice Cream Manufacturing",
            description = "A small-scale ice cream manufacturing unit produces a variety of ice creams ...",
            budget_range = "500000 - 700000",
            category_name = "Manufacturing (Food)",
            rawMaterials = listOf(
                mapOf("title" to "Manufacturing Machinery (Batch freezer, pasteurizer, homogenizer)", "price" to 300000),
                mapOf("title" to "Deep Freezers & Cold Storage", "price" to 150000),
                mapOf("title" to "Initial Raw Materials & Packaging", "price" to 75000),
                mapOf("title" to "Workspace Setup & Rent Deposit", "price" to 75000),
                mapOf("title" to "FSSAI License & Other Registrations", "price" to 25000),
                mapOf("title" to "Local Marketing & Distribution", "price" to 25000)
            )
        ),
        Adminbusinessidea(
                businessName = "PVC Pipe Manufacturing",
        description = "This business involves the manufacturing of Polyvinyl Chloride (PVC) pipes used extensively in agriculture for irrigation, as well as in construction for plumbing and drainage.\n\nTarget Customers\nHardware stores and plumbing suppliers.\nConstruction companies and real estate developers.\nFarmers and agricultural suppliers.\nGovernment contracts for infrastructure projects.\n\nKey Financial Metrics\nGross Profit Margin: 20-35% (Volume-driven business).\nNet Profit Margin: 8-15%",
        budget_range = "500000 - 700000",
        category_name = "Manufacturing",
        rawMaterials = listOf(
            mapOf("title" to "Extrusion Machine (Used or basic model)", "price" to 350000),
            mapOf("title" to "Dies, Mixer, and other auxiliary equipment", "price" to 150000),
            mapOf("title" to "Workspace Rent Deposit & Setup", "price" to 75000),
            mapOf("title" to "Initial Raw Material (PVC resin, additives)", "price" to 75000),
            mapOf("title" to "Licenses & Registrations", "price" to 25000),
            mapOf("title" to "Working Capital", "price" to 25000)
        )
    ),
    Adminbusinessidea(
    businessName = "Fast Food Franchise (Local Brand)",
    description = "This involves acquiring the rights to operate a franchise of an established local or regional fast-food brand. The franchisee gets access to the brand name, menu, operating procedures, and supply chain in exchange for a franchise fee and ongoing royalties.\n\nTarget Customers\nStudents and young adults.\nFamilies looking for quick and affordable meals.\nShoppers in high-traffic areas.\n\nKey Financial Metrics\nGross Profit Margin: 50-65%\nNet Profit Margin: 10-18% (Royalty fees impact net margin).",
    budget_range = "500000 - 700000",
    category_name = "Food & Beverage",
    rawMaterials = listOf(
    mapOf("title" to "Franchise Fee", "price" to 150000),
    mapOf("title" to "Outlet Interior & Setup (As per brand standards)", "price" to 200000),
    mapOf("title" to "Kitchen Equipment (Provided or specified by franchisor)", "price" to 150000),
    mapOf("title" to "Rent Deposit", "price" to 75000),
    mapOf("title" to "Initial Inventory & Working Capital", "price" to 50000)
    )
    ),
    Adminbusinessidea(
    businessName = "Cyber Cafe",
    description = "A cyber cafe provides public access to computers and the internet for a fee. Modern cyber cafes often diversify their services to include printing, scanning, online form filling, gaming, and sometimes a small snack counter.\n\nTarget Customers\nStudents for research, projects, and exam form submissions.\nIndividuals without personal computers or internet access.\nGamers looking for a high-speed gaming environment.\nJob seekers for online applications.\nTourists and travelers.\n\nKey Financial Metrics\nGross Profit Margin: 70-85% (Main cost is internet and electricity).\nNet Profit Margin: 25-40% (Depends on rent and system maintenance).",
    budget_range = "50000 - 100000",
    category_name = "Services",
    rawMaterials = listOf(
    mapOf("title" to "Computers (Refurbished desktops, 5-8 units)", "price" to 40000),
    mapOf("title" to "Networking & Internet Setup (Router, switches, initial plan)", "price" to 10000),
    mapOf("title" to "Furniture (Tables, chairs)", "price" to 15000),
    mapOf("title" to "Printer & Scanner (All-in-one)", "price" to 15000),
    mapOf("title" to "Software Licenses & Basic Setup", "price" to 5000),
    mapOf("title" to "Shop Rental Advance & Signage", "price" to 15000)
    )
    ),
    Adminbusinessidea(
    businessName = "Organic Fruits & Vegetables Store",
    description = "This retail business focuses on selling certified organic fruits and vegetables. The key is to build a reliable supply chain by sourcing directly from organic farms and educating customers about the benefits of organic produce.\n\nTarget Customers\nHealth-conscious individuals and families.\nHigh-income households.\nFitness enthusiasts and yoga practitioners.\nRestaurants and cafes focusing on healthy menus.\n\nKey Financial Metrics\nGross Profit Margin: 30-50% (Higher than conventional produce but also higher sourcing cost).\nNet Profit Margin: 10-20% (Perishability and spoilage are key factors).",
    budget_range = "150000 - 300000",
    category_name = "Retail",
    rawMaterials = listOf(
    mapOf("title" to "Shop Rental Deposit & Interior (Racks, bins)", "price" to 75000),
    mapOf("title" to "Refrigeration & Cooling Units", "price" to 50000),
    mapOf("title" to "Initial Stock of Produce", "price" to 50000),
    mapOf("title" to "Weighing Scales & POS System", "price" to 20000),
    mapOf("title" to "Marketing & Local Promotion (Pamphlets, social media)", "price" to 15000),
    mapOf("title" to "Licenses & Registrations (Shop Act, FSSAI)", "price" to 10000),
    mapOf("title" to "Working Capital (for daily procurement)", "price" to 30000)
    )
    ),
    Adminbusinessidea(
    businessName = "Ice Cream Manufacturing",
    description = "A small-scale ice cream manufacturing unit produces a variety of ice creams ...",
    budget_range = "500000 - 700000",
    category_name = "Manufacturing (Food)",
    rawMaterials = listOf(
    mapOf("title" to "Manufacturing Machinery (Batch freezer, pasteurizer, homogenizer)", "price" to 300000),
    mapOf("title" to "Deep Freezers & Cold Storage", "price" to 150000),
    mapOf("title" to "Initial Raw Materials & Packaging", "price" to 75000),
    mapOf("title" to "Workspace Setup & Rent Deposit", "price" to 75000),
    mapOf("title" to "FSSAI License & Other Registrations", "price" to 25000),
    mapOf("title" to "Local Marketing & Distribution", "price" to 25000)
    )
    ),
        Adminbusinessidea(
            businessName = "Health Drinks (Juice & Smoothie Bar)",
            description = "This business involves setting up a kiosk or small shop that prepares and sells fresh, healthy drinks like fruit juices, vegetable juices, smoothies, and protein shakes. The focus is on using natural ingredients without artificial additives.\n\nTarget Customers\nGym-goers and fitness enthusiasts.\nOffice workers looking for a healthy lunch alternative.\nShoppers in malls and high-street areas.\nStudents in college areas.\n\nKey Financial Metrics\nGross Profit Margin: 50-70%\nNet Profit Margin: 20-35%",
            budget_range = "300000 - 500000",
            category_name = "Food & Beverage",
            rawMaterials = listOf(
                mapOf("title" to "Kiosk/Shop Fit-out & Branding", "price" to 100000),
                mapOf("title" to "Equipment (Commercial juicers, blenders, refrigeration, ice machine)", "price" to 150000),
                mapOf("title" to "Rent Deposit for a good location", "price" to 75000),
                mapOf("title" to "Initial Inventory (Fruits, vegetables, protein powders, packaging)", "price" to 40000),
                mapOf("title" to "POS System & Software", "price" to 20000),
                mapOf("title" to "Licenses & Permits (FSSAI, etc.)", "price" to 15000)
            )
        ),
        Adminbusinessidea(
            businessName = "Small Garment Unit (T-Shirts)",
            description = "Unlike just printing, this unit focuses on the complete manufacturing of T-shirts. The process involves sourcing fabric, cutting it into patterns, stitching the pieces together, and then finishing the garments. It can also include an in-house printing or embroidery section.\n\nTarget Customers\nClothing brands and startups needing manufacturing partners.\nCorporate clients for bulk uniform orders.\nExporters and wholesale garment traders.\n\nKey Financial Metrics\nGross Profit Margin: 25-40%\nNet Profit Margin: 10-20% (Volume-dependent).",
            budget_range = "300000 - 500000",
            category_name = "Manufacturing",
            rawMaterials = listOf(
                mapOf("title" to "Sewing & Stitching Machines (Industrial grade, 5-7 units)", "price" to 150000),
                mapOf("title" to "Fabric Cutting Machine & Tables", "price" to 50000),
                mapOf("title" to "Initial Fabric & Thread Stock", "price" to 100000),
                mapOf("title" to "Workspace Rental Deposit & Setup", "price" to 75000),
                mapOf("title" to "Initial Labor Costs (Salaries for cutters, tailors)", "price" to 50000),
                mapOf("title" to "Licenses & Registrations", "price" to 25000)
            )
        ),
        Adminbusinessidea(
            businessName = "Stationery & Printing Shop",
            description = "This is a retail shop that sells office and school supplies, along with offering services like photocopying, printing, lamination, and binding. It's a high-demand business, especially if located near schools, colleges, or business districts.\n\nTarget Customers\nStudents and teachers.\nLocal offices and small businesses.\nGeneral public for daily needs.\n\nKey Financial Metrics\nGross Profit Margin: 25-40% on products, 60-80% on services.\nNet Profit Margin: 15-25%",
            budget_range = "100000 - 200000",
            category_name = "Retail",
            rawMaterials = listOf(
                mapOf("title" to "Initial Inventory (Pens, paper, notebooks, etc.)", "price" to 75000),
                mapOf("title" to "Photocopier & Printer (Heavy-duty multifunction machine)", "price" to 60000),
                mapOf("title" to "Shop Rental Deposit & Racks/Shelving", "price" to 40000),
                mapOf("title" to "Computer & Basic Software", "price" to 20000),
                mapOf("title" to "Lamination & Binding Machines", "price" to 10000)
            )
        ),
        Adminbusinessidea(
            businessName = "Drone Photography",
            description = "This service-based business uses drones (UAVs) to capture high-quality aerial photos and videos. It's a rapidly growing field with diverse applications.\n\nTarget Customers\nReal estate agents and developers for property showcases.\nWedding and event planners for aerial shots.\nConstruction companies for site monitoring and inspection.\nMedia and film production houses.\nTourism and hospitality sector for promotional videos.\n\nKey Financial Metrics\nGross Profit Margin: 70-90% (Service-based).\nNet Profit Margin: 40-60%",
            budget_range = "300000 - 500000",
            category_name = "Services",
            rawMaterials = listOf(
                mapOf("title" to "Professional Drones (2 high-quality units with extra batteries)", "price" to 250000),
                mapOf("title" to "Camera Gear & Gimbals", "price" to 50000),
                mapOf("title" to "Editing Software & High-Performance Computer", "price" to 75000),
                mapOf("title" to "Licenses & Pilot Certification (DGCA compliance)", "price" to 30000),
                mapOf("title" to "Marketing (Website, portfolio, social media ads)", "price" to 45000)
            )
        ),
        Adminbusinessidea(
            businessName = "Toy Manufacturing",
            description = "A small-scale toy manufacturing business that can focus on a specific niche, such as wooden toys, educational toys, or soft toys. The emphasis can be on safety, local craftsmanship, and unique designs.\n\nTarget Customers\nToy retailers and wholesalers.\nSchools and playschools (for educational toys).\nDirect-to-consumer via online stores (like Amazon, Flipkart) and local fairs.\n\nKey Financial Metrics\nGross Profit Margin: 35-50%\nNet Profit Margin: 15-25%",
            budget_range = "300000 - 500000",
            category_name = "Manufacturing",
            rawMaterials = listOf(
                mapOf("title" to "Molds & Machinery (Depending on toy type, e.g., injection molding for plastic, woodworking tools for wooden)", "price" to 200000),
                mapOf("title" to "Raw Materials (Plastic granules, wood, non-toxic paints, fabric)", "price" to 100000),
                mapOf("title" to "Workspace Rental Deposit & Setup", "price" to 75000),
                mapOf("title" to "Safety Certifications (BIS compliance is mandatory)", "price" to 40000),
                mapOf("title" to "Packaging Design & Initial Stock", "price" to 35000)
            )
        ),
        Adminbusinessidea(
            businessName = "Fresh Juice & Smoothie Stall",
            description = "This is a small-scale, mobile, or fixed stall business model focused on serving fresh juices and smoothies on the go. It requires a smaller investment than a full cafe and thrives in high-footfall areas.\n\nTarget Customers\nCommuters near bus or train stations.\nMorning walkers and joggers in parks.\nShoppers in local markets.\nEmployees near corporate parks.\n\nKey Financial Metrics\nGross Profit Margin: 60-75%\nNet Profit Margin: 30-45% (Lower overheads compared to a shop).",
            budget_range = "50000 - 100000",
            category_name = "Food & Beverage",
            rawMaterials = listOf(
                mapOf("title" to "Stall/Kiosk Fabrication & Branding", "price" to 30000),
                mapOf("title" to "Equipment (Juicers, blenders, small refrigerator/ice box)", "price" to 35000),
                mapOf("title" to "Initial Inventory (Fruits, vegetables, disposables)", "price" to 15000),
                mapOf("title" to "Licenses & Permits (Local municipal permission, FSSAI)", "price" to 10000),
                mapOf("title" to "Working Capital", "price" to 10000)
            )
        ),
        Adminbusinessidea(
            businessName = "Tech Gadget Rental Service",
            description = "This business rents out tech gadgets like high-end cameras, drones, projectors, gaming consoles, and virtual reality (VR) headsets for short periods. It caters to customers who need specific gadgets for a project, event, or trial but don't want to purchase them.\n\nTarget Customers\nPhotographers and videographers for specific projects.\nEvent organizers for conferences and parties.\nCorporates for presentations and short-term needs.\nIndividuals for travel, parties, or trying out new technology.\n\nKey Financial Metrics\nGross Profit Margin: 80-90% (Revenue per rental vs. depreciation of the asset).\nNet Profit Margin: 30-50% (Depends on asset utilization and maintenance).",
            budget_range = "300000 - 500000",
            category_name = "Services",
            rawMaterials = listOf(
                mapOf("title" to "Initial Gadget Inventory (DSLRs, lenses, drones, projectors, etc.)", "price" to 350000),
                mapOf("title" to "Website & Online Booking Platform", "price" to 40000),
                mapOf("title" to "Insurance for Equipment", "price" to 30000),
                mapOf("title" to "Small Office/Storage Space Deposit", "price" to 30000),
                mapOf("title" to "Marketing & Promotion", "price" to 30000),
                mapOf("title" to "Legal (Rental agreements, business registration)", "price" to 20000)
            )
        ),
        Adminbusinessidea(
            businessName = "Ready-to-Eat Snacks (Chips & Namkeen) Manufacturing",
            description = "This is a manufacturing business focused on producing and packaging popular Indian snacks like potato chips, banana chips, and various types of namkeen (savory mixtures).\n\nTarget Customers\nLocal distributors and wholesalers.\nSupermarkets, hypermarkets, and local kirana stores.\nHotels, canteens, and tea stalls.\n\nKey Financial Metrics\nGross Profit Margin: 30-45%\nNet Profit Margin: 10-20% (A volume-driven business).",
            budget_range = "700000 - 1000000",
            category_name = "Manufacturing",
            rawMaterials = listOf(
                mapOf("title" to "Machinery (Slicer, fryer, spice mixer, packaging machine)", "price" to 500000),
                mapOf("title" to "Initial Raw Materials (Potatoes, gram flour, spices, oil, packaging film)", "price" to 150000),
                mapOf("title" to "Workspace Rental Deposit & Setup", "price" to 150000),
                mapOf("title" to "Licenses (FSSAI, GST, Factory License) & Lab Testing", "price" to 75000),
                mapOf("title" to "Working Capital & Distribution Logistics", "price" to 125000)
            )
        ),
        Adminbusinessidea(
            businessName = "Robotics Training & Workshop Center",
            description = "This business provides hands-on training in robotics, coding, and STEM (Science, Technology, Engineering, and Mathematics) concepts, primarily for school students. It involves creating a curriculum and conducting regular classes, workshops, and competitions.\n\nTarget Customers\nSchool students (Ages 8-18).\nEngineering students looking for practical skills.\nSchools looking to outsource their robotics curriculum.\nHobbyists and tech enthusiasts.\n\nKey Financial Metrics\nGross Profit Margin: 60-75%\nNet Profit Margin: 25-40%",
            budget_range = "500000 - 700000",
            category_name = "Education",
            rawMaterials = listOf(
                mapOf("title" to "Robotics Kits & Components (Arduino, Raspberry Pi, sensors, motors)", "price" to 250000),
                mapOf("title" to "Computers & Software", "price" to 100000),
                mapOf("title" to "Classroom/Lab Setup (Furniture, projectors, tools)", "price" to 100000),
                mapOf("title" to "Rental Deposit for Commercial Space", "price" to 75000),
                mapOf("title" to "Curriculum Development & Marketing", "price" to 50000),
                mapOf("title" to "Business Registration", "price" to 25000)
            )
        ),
        Adminbusinessidea(
            businessName = "Paper Bag Making",
            description = "This is a small-scale manufacturing unit focused solely on making paper bags. Given the low budget, the focus would be on manual or semi-automatic processes to cater to local businesses.\n\nTarget Customers\nLocal boutiques, bakeries, and pharmacies.\nGrocery and sweet shops.\nSmall businesses needing custom-branded bags.\n\nKey Financial Metrics\nGross Profit Margin: 30-45%\nNet Profit Margin: 15-25%",
            budget_range = "50000 - 100000",
            category_name = "Manufacturing",
            rawMaterials = listOf(
                mapOf("title" to "Manual Paper Bag Making Machine & Tools", "price" to 25000),
                mapOf("title" to "Initial Raw Material (Paper sheets/rolls, glue, handles)", "price" to 30000),
                mapOf("title" to "Workspace Rental Deposit (Small room or shed)", "price" to 20000),
                mapOf("title" to "Screen Printing Setup (for simple branding)", "price" to 15000),
                mapOf("title" to "Working Capital", "price" to 10000)
            )
        ),
        Adminbusinessidea(
            businessName = "Herbal Cosmetics",
            description = "This business involves manufacturing and selling cosmetic products made from natural and herbal ingredients. The product line can include face creams, soaps, hair oils, and face packs, tapping into the growing demand for chemical-free products.\n\nTarget Customers\nHealth-conscious consumers.\nCustomers in local markets and through online platforms.\nBeauty salons and spas looking for natural product lines.\n\nKey Financial Metrics\nGross Profit Margin: 40-60%\nNet Profit Margin: 20-35%",
            budget_range = "100000 - 300000",
            category_name = "Manufacturing",
            rawMaterials = listOf(
                mapOf("title" to "Basic Lab & Mixing Equipment (Beakers, mixers, weighing scales)", "price" to 50000),
                mapOf("title" to "Initial Raw Materials (Herbs, oils, waxes, extracts)", "price" to 75000),
                mapOf("title" to "Packaging (Bottles, jars, labels)", "price" to 50000),
                mapOf("title" to "Licenses (Ayurveda/Cosmetic license, GMP) & Testing", "price" to 50000),
                mapOf("title" to "Workspace Rental & Setup", "price" to 40000),
                mapOf("title" to "Branding & Marketing", "price" to 35000)
            )
        ),
        Adminbusinessidea(
            businessName = "Digital Marketing Agency",
            description = "This is a lean, service-based agency providing digital marketing solutions like social media management (SMM), search engine optimization (SEO), content creation, and online advertising for small businesses.\n\nTarget Customers\nLocal businesses (restaurants, clinics, shops) wanting to build an online presence.\nStartups on a tight marketing budget.\nIndividual professionals (doctors, lawyers).\n\nKey Financial Metrics\nGross Profit Margin: 80-90%\nNet Profit Margin: 50-70% (Low overheads, can be run from home).",
            budget_range = "50000 - 100000",
            category_name = "Services",
            rawMaterials = listOf(
                mapOf("title" to "Laptop & High-Speed Internet", "price" to 50000),
                mapOf("title" to "Subscription to Marketing Tools (Canva, Buffer, SEMrush - basic plans)", "price" to 15000),
                mapOf("title" to "Website & Portfolio Development", "price" to 10000),
                mapOf("title" to "Online Advertising (for the agency itself)", "price" to 15000),
                mapOf("title" to "Business Registration", "price" to 10000)
            )
        ),
        Adminbusinessidea(
            businessName = "Small Bakery (Cookies & Cake)",
            description = "A small bakery, often run from a commercial kitchen or a small retail space, focusing on a niche menu of cookies, cakes, cupcakes, and brownies. It can operate on a pre-order basis or have a small display for walk-in customers.\n\nTarget Customers\nLocal residents for birthdays, anniversaries, and special occasions.\nCorporate offices for events.\nCafes and restaurants to supply desserts.\n\nKey Financial Metrics\nGross Profit Margin: 50-70%\nNet Profit Margin: 20-35%",
            budget_range = "100000 - 300000",
            category_name = "Food & Beverage",
            rawMaterials = listOf(
                mapOf("title" to "Baking Equipment (Oven, planetary mixer, work tables)", "price" to 125000),
                mapOf("title" to "Refrigeration & Display Case", "price" to 50000),
                mapOf("title" to "Shop/Kitchen Rental Deposit & Basic Setup", "price" to 50000),
                mapOf("title" to "Initial Inventory (Flour, sugar, butter, chocolate, packaging)", "price" to 40000),
                mapOf("title" to "Licenses (FSSAI, GST, Shop Act)", "price" to 20000),
                mapOf("title" to "POS & Marketing", "price" to 15000)
            )
        ),
        Adminbusinessidea(
            businessName = "LED Bulb Assembly Unit",
            description = "This business involves assembling LED bulbs by sourcing components like the housing, driver, PCB with LEDs, and diffuser. It's a relatively simple assembly process that can be done with minimal heavy machinery.\n\nTarget Customers\nElectrical hardware wholesalers and retailers.\nCorporate clients for bulk orders.\nGovernment tenders for lighting projects.\n\nKey Financial Metrics\nGross Profit Margin: 15-25%\nNet Profit Margin: 8-15% (High volume is key).",
            budget_range = "700000 - 1000000",
            category_name = "Manufacturing",
            rawMaterials = listOf(
                mapOf("title" to "Assembly & Testing Machinery (Soldering machines, testers, sealing machine)", "price" to 250000),
                mapOf("title" to "Initial Stock of Components (SKD kits)", "price" to 500000),
                mapOf("title" to "Workspace Rental Deposit & Setup (Assembly line tables)", "price" to 100000),
                mapOf("title" to "Licenses & BIS Certification (Mandatory)", "price" to 100000),
                mapOf("title" to "Working Capital", "price" to 50000)
            )
        )
    )

}
