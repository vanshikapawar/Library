document.addEventListener("DOMContentLoaded", function() {
	
	fetchAndUpdateBookList();
	
	const emailField = document.getElementById("emaill");
	    if (emailField) {
	        emailField.addEventListener("blur", fetchCustomerName);
	    }

    //registering new customers
    const registerForm = document.getElementById("customer-form");
    if (registerForm) {
        registerForm.addEventListener("submit", function(event) {
            event.preventDefault(); 
            
            const formData = new FormData(registerForm);
            const custName = formData.get("custName");
			const age = formData.get("age");
            const mobile_no = formData.get("custMobile");
			const email = formData.get("email");
            const custAddress = formData.get("custAddress");


            fetch("/customer", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    cust_name: custName,
					age: age,
                    mobile_no: mobile_no,
					email: email,
                    cust_address: custAddress
                })
            })
            .then(response => {
                if (response.ok) {
                    console.log("in the console check");
                    alert("Customer registered successfully!");
                    registerForm.reset(); 
                } else {
                    
                    alert("Failed to register customer. Please try again.");
                }
            })
            .catch(error => {
                console.error("Error registering customer:", error);
                alert("An unexpected error occurred. Please try again later.");
            });
        });
    }
    
    
    // Add a new book
    const addBookForm = document.getElementById("add-book-section");
    if (addBookForm) {
        addBookForm.addEventListener("submit", function(event) {
            event.preventDefault(); 
            
            const formData = new FormData(addBookForm);
            const book_name = formData.get("title");
            const author = formData.get("author");
            const genre = formData.get("genre");
			const total_stock = formData.get("stock");
            const available_copies = formData.get("copies");
  
            fetch("/books", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    book_name: book_name,
                    author: author,
                    genre: genre,
					available_copies: available_copies,
                    total_stock: total_stock
                })
            })
            .then(response => {
                if (response.ok) {
                    fetchAndUpdateBookList();
                    alert("Book added successfully!");
                    addBookForm.reset(); 
                } else {
                    
                    alert("Failed to add book. Please try again.");
                }
            })
            .catch(error => {
                console.error("Error adding book:", error);
                alert("An unexpected error occurred. Please try again later.");
            });
        });
    }
    
    function fetchAndUpdateBookList() {
    fetch("/books")
    .then(response => response.json())
    .then(data => {
		console.log("data form the book table", data);
        const bookTableBody = document.getElementById("bookTableBody");
		console.log("the book table body", bookTableBody);
        bookTableBody.innerHTML = "";

        data.forEach(books => {
            const newRow = document.createElement('tr');
            newRow.innerHTML = `
                <td>${books.book_id}</td>
                <td>${books.book_name}</td>
                <td>${books.author}</td>
                <td>${books.genre}</td>
                <td>${books.total_stock}</td>
				<td>${books.available_copies}</td>
                <!-- Add more <td> elements for other book attributes -->
            `;
            bookTableBody.appendChild(newRow);
			console.log("the new row",newRow);
        });
    })
    .catch(error => console.error("Error fetching book list:", error));
}

//deleting a book
const deleteBookForm = document.getElementById("delete-book-form");
if (deleteBookForm) {
    deleteBookForm.addEventListener("submit", function(event) {
        event.preventDefault(); 
        
        const formData = new FormData(deleteBookForm);
        const book_name = formData.get("bookName");
		const author = formData.get("author");

        fetch(`/books?book_name=${book_name}&author=${author}`, {  
            method: "DELETE"
        })
        .then(response => {
            if (response.ok) {
				fetchAndUpdateBookList();
                alert("Book deleted successfully!");
                deleteBookForm.reset(); 
            } else if (response.status === 404) {
                alert("Book not found.");
            } else {
                alert("An unexpected error occurred. Please try again later.");
            }
        })
        .catch(error => {
            console.error("Error deleting book:", error);
            alert("An unexpected error occurred. Please try again later.");
        });
    });
}


	//issue book
    const issueBookForm = document.getElementById("issue-book-form");
    if (issueBookForm) {
        issueBookForm.addEventListener("submit", function(event) {
            event.preventDefault(); 
            

            const formData = new FormData(issueBookForm);
            const book_name = formData.get("book_name");
            const cust_name = formData.get("cust_name");
			const email = formData.get("email");

            fetch(`/issuebook?book_name=${book_name}&cust_name=${cust_name}&email=${email}`, {
                method: "POST"
            })
            .then(response => {
                if (response.ok) {
                    fetchAndUpdateBookList();
                    alert("Book issued successfully!");
                    issueBookForm.reset(); 
                } else if (response.status === 400) {
                    
                    alert("No available copies of the book");
                } else if (response.status === 404) {
                    
                    alert("Book or customer not found.");
                } else {
                 
                    alert("An unexpected error occurred. Please try again later.");
                }
            })
            .catch(error => {
                console.error("Error issuing book:", error);
                alert("An unexpected error occurred. Please try again later.");
            });
        });
    }




   /*
    //search customers
    const searchForm = document.getElementById("search-form");
    if (searchForm) {
        searchForm.addEventListener("submit", function(event) {
            event.preventDefault(); 
            
 
            const formData = new FormData(searchForm);
            const userId = formData.get("userId");


            fetch(/customers/search?userId=${userId})
            .then(response => {
                if (response.ok) {
                   
                } else if (response.status === 404) {
                    
                    alert("Customer not found.");
                } else {
                    
                    alert("An unexpected error occurred. Please try again later.");
                }
            })
            .catch(error => {
                console.error("Error searching customer:", error);
                alert("An unexpected error occurred. Please try again later.");
            });
        });
    }
    
    */
    
    const returnBookForm = document.getElementById("return-book-form");
	if (returnBookForm) {
    returnBookForm.addEventListener("submit", function(event) {
        event.preventDefault(); 
        

        const formData = new FormData(returnBookForm);
        const book_name = formData.get("book_name");
        const cust_name = formData.get("cust_name");
		const email = formData.get("email");


        fetch(`/returnbook?book_name=${book_name}&cust_name=${cust_name}&email=${email}`, {
            method: "POST"
        })
		.then(response => response.json())  // Parse the JSON response
		.then(data => {
		    if (data.status === "success") {  // Check the "status" field in the JSON response
		        fetchAndUpdateBookList();
		        alert(data.message);  // Use the message from the response
		        returnBookForm.reset(); 
		    } else if (data.status === "error") {
		        alert(data.message);  // Use the error message from the response
		    } else {
		        alert("An unexpected error occurred. Please try again later.");
		    }
        })
        .catch(error => {
            console.error("Error returning book:", error);
            alert("An unexpected error occurred. Please try again later.");
        });
    });
}



function fetchCustomerName() {
    const email = document.getElementById("emaill").value;
	console.log("fetchCustomerName triggered");
    if (email) {
      fetch(`/byEmail?email=${encodeURIComponent(email)}`)
        .then(response => response.json())
        .then(data => {
          document.getElementById("cust_name").value = data.name || "Customer not found";
        })
        .catch(error => {
          console.error('Error fetching customer:', error);
        });
    }
  }

// Search functionality
const searchForm = document.getElementById("searchForm");
const searchResultsSection = document.getElementById("search-results-section");
const bookTableBody = document.getElementById("searchbookTableBody");

searchForm.addEventListener("submit", function(event) {
    event.preventDefault();
    
    const searchQuery = document.getElementById("searchQuery").value.trim(); // Trim whitespace

    // Make a fetch request to search for books
    fetch(`/search?query=${encodeURIComponent(searchQuery)}`) // Corrected endpoint
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Clear any previous search results
                bookTableBody.innerHTML = "";

                // Display the results in the table
                data.forEach(books => {
                    const newRow = document.createElement('tr');
                    newRow.innerHTML = `
                        <td>${books.book_id}</td>
                        <td>${books.book_name}</td>
                        <td>${books.author}</td>
                        <td>${books.genre}</td>
                        <td>${books.total_stock}</td>
                        <td>${books.available_copies}</td>
                    `;
                    bookTableBody.appendChild(newRow);
                });

                // Show the search result table
                searchResultsSection.style.display = "block";
            } else {
                alert("No books found matching your search query.");
                searchResultsSection.style.display = "none"; // Hide table if no results
            }
        })
        .catch(error => {
            console.error("Error fetching book search results:", error);
            alert("An error occurred while searching for books. Please try again.");
            searchResultsSection.style.display = "none"; // Hide table if there's an error
        });
});


// Add Event functionality
const addEventForm = document.getElementById("add-event-form");

addEventForm.addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent default form submission

    // Collect form data
    const eventName = document.getElementById("event_name").value.trim();
    const details = document.getElementById("details").value.trim();
    const eventDate = document.getElementById("event_date").value;
    const venue = document.getElementById("venue").value.trim();
    const time = document.getElementById("time").value;

    // Check if any fields are empty
    if (!eventName || !details || !eventDate || !venue || !time) {
        alert("Please fill in all required fields.");
        return;
    }

    // Create the event object
    const eventData = {
        event_name: eventName,
        details: details,
        event_date: eventDate,
        venue: venue,
        time: time
    };

    // Make a fetch request to add the event
    fetch('/eventAdd', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(eventData)
    })
    .then(response => {
        if (response.ok) {
            alert("Event added successfully!");
            addEventForm.reset(); // Reset the form fields
        } else {
            throw new Error('Failed to add event');
        }
    })
    .catch(error => {
        console.error("Error adding event:", error);
        alert("An error occurred while adding the event. Please try again.");
    });
});


    
});