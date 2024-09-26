document.addEventListener("DOMContentLoaded", function() {
	
	fetchAndUpdateBookList();
	
	fetchAndUpdateEventList();
	
	const emailField = document.getElementById("emaill");
	    if (emailField) {
	        emailField.addEventListener("blur", fetchCustomerName);
	    }
		
		const emailCust = document.getElementById("cust_email");
		if(emailCust){
			emailCust.addEventListener("blur",fetchCustName);
		}
		
		const amtByEmail = document.getElementById("cust_namee");
		if(amtByEmail){
			amtByEmail.addEventListener("blur",fetchAmountToPay);
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

			if (!/^[A-Za-z\s]+$/.test(custName)) {
			            alert("Customer name should only contain alphabets and spaces.");
			            return;
			        }
			
			if (!validateEmail(email)) {
			            alert("Please enter a valid email address.");
			            return;
			        }

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
    
	function validateEmail(email) {
	    // Regular expression for validating email
	    const emailPattern = /^[a-zA-Z][a-zA-Z0-9._%+-]*@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

	    if (!emailPattern.test(email)) {
	        return false;
	    }

	    const atIndex = email.indexOf('@');
	    const dotIndex = email.indexOf('.', atIndex);
	    
	    // Check if '@' comes before '.'
	    if (atIndex === -1 || dotIndex === -1 || atIndex > dotIndex) {
	        return false;
	    }
	    
	    // Check for special characters and that '@' or '.' is not the first character
	    if (/[!#$%^&*(),?":{}|<>]/.test(email) || ['@', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'].includes(email[0])) {
	        return false;
	    }

	    return true;
	}
    
    // Add a new book
    const addBookForm = document.getElementById("add-book-section");
	const addToExistingCheckbox = document.getElementById("add-to-existing");
    if (addBookForm) {
        addBookForm.addEventListener("submit", function(event) {
            event.preventDefault(); 
            
            const formData = new FormData(addBookForm);
            const book_name = formData.get("title");
            const author = formData.get("author");
            const genre = formData.get("genre");
			const total_stock = formData.get("stock");
            const available_copies = formData.get("copies");
			const addToExisting = addToExistingCheckbox.checked;
			
			if (!/^[A-Za-z\s.]+$/.test(author)) {
						            alert("Author name should only contain alphabets and spaces.");
						            return;
						        }
								
								if (!/^[A-Za-z\s,]+$/.test(genre)) {
								            alert("Genre should only contain alphabets, spaces, and commas.");
								            return;
								        }
										
										if (total_stock < available_copies) {
										            alert("Total stock must be equal to or greater than available copies. Please check the entries");
										            return;
										        }						
  
            fetch(addToExisting ? "/addBookCopies" : "/books", {
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
                    alert(addToExisting ? "Book copies updated successfully!" : "Book added successfully!");
                    addBookForm.reset(); 
                } else {
                    
                    alert("Failed to add book. Please check the fields entried and try again.");
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



const customRemoveCheckbox = document.getElementById("customRemove");
    const numCopiesDiv = document.getElementById("numCopiesDiv");

    // Show or hide the input for number of copies based on checkbox selection
    customRemoveCheckbox.addEventListener("change", function() {
        if (this.checked) {
            numCopiesDiv.style.display = "block";  // Show the input when checkbox is checked
        } else {
            numCopiesDiv.style.display = "none";   // Hide the input when checkbox is unchecked
        }
    });

//deleting a book
const deleteBookForm = document.getElementById("delete-book-form");
if (deleteBookForm) {
    deleteBookForm.addEventListener("submit", function(event) {
        event.preventDefault(); 
        
        const formData = new FormData(deleteBookForm);
        const book_name = formData.get("bookName");
		const author = formData.get("author");
		
		if (!/^[A-Za-z\s.]+$/.test(author)) {
								            alert("Author name should only contain alphabets and spaces.");
								            return;
								        }
		
		const customRemove = formData.get("customRemove"); // Checkbox value
		        const numCopies = formData.get("numCopies"); // Number of copies to remove

				// Create URL parameters based on user selection
				        let url = `/books?book_name=${book_name}&author=${author}`;
				        
				        // Check if the user selected to remove a custom number of copies
				        if (customRemove && numCopies) {
				            url += `&customRemove=true&numCopies=${numCopies}`;
				        }

				
        /*fetch(`/books?book_name=${book_name}&author=${author}`, {  
            method: "DELETE"
        })*/
		fetch(url, {  
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
                alert("Please check the values of the field entered as required book not founr");
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
	const reissuebook = document.getElementById("re-issue");
    if (issueBookForm) {
        issueBookForm.addEventListener("submit", function(event) {
            event.preventDefault(); 
            

            const formData = new FormData(issueBookForm);
            const book_name = formData.get("book_name");
            const cust_name = formData.get("cust_name");
			const email = formData.get("email");
			const reissue = reissuebook.checked;
			
			if (!validateEmail(email)) {
						            alert("Please enter a valid email address.");
						            return;
						        }
								const url = reissue ? `/reissuebook` : `/issuebook`;
            fetch(`${url}?book_name=${book_name}&cust_name=${cust_name}&email=${email}`, {
                method: "POST"
            })
            .then(response => {
                if (response.ok) {
                    fetchAndUpdateBookList();
                    alert(reissue ? "Book reissued successfully!" : "Book issued successfully!");
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
    const returnBookForm = document.getElementById("return-book-form");
	if (returnBookForm) {
    returnBookForm.addEventListener("submit", function(event) {
        event.preventDefault(); 
        

        const formData = new FormData(returnBookForm);
        const book_name = formData.get("book_name");
        const cust_name = formData.get("cust_name");
		const email = formData.get("email");

		if (!validateEmail(email)) {
					            alert("Please enter a valid email address.");
					            return;
					        }
		
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
}*/


const returnBookForm = document.getElementById("return-book-form");
const submitReturnBtn = document.getElementById("submit_return_btn");
const amountConfirmRadio = document.getElementById("amount_confirm_radio");


if (returnBookForm) {
    returnBookForm.addEventListener("submit", function(event) {
        event.preventDefault(); 

        const formData = new FormData(returnBookForm);
        const bookName = formData.get("book_name");
        const customerName = formData.get("cust_name");
        const customerEmail = formData.get("email");
        const isAmountPaid = amountConfirmRadio.checked;  // Check if the amount has been confirmed as paid

        if (!validateEmail(customerEmail)) {
            alert("Please enter a valid email address.");
            return;
        }

        if (!isAmountPaid) {
            alert("Please confirm that the amount has been paid.");
            return;
        }

        fetch(`/returnbook?book_name=${bookName}&cust_name=${customerName}&email=${customerEmail}`, {
            method: "POST"
        })
        .then(response => response.json())  // Parse the JSON response
        .then(data => {
            if (data.status === "success") {  // Check the "status" field in the JSON response
                fetchAndUpdateBookList();
                alert(data.message);  // Use the message from the response
                returnBookForm.reset();
                submitReturnBtn.disabled = true;  // Disable the button again after success
                amountConfirmRadio.checked = false;  // Reset the radio button
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

function confirmAmountPaid() {
    // Enable the Return Book button only if the amount is confirmed as paid
    submitReturnBtn.disabled = !amountConfirmRadio.checked;
}

// Add event listener for the radio button to call confirmAmountPaid
if (amountConfirmRadio) {
    amountConfirmRadio.addEventListener("change", confirmAmountPaid);
}


function fetchCustName() {
    const email = document.getElementById("cust_email").value;
    if (email) {
      fetch(`/byEmail?email=${encodeURIComponent(email)}`)
        .then(response => response.json())
        .then(data => {
          document.getElementById("cust_namee").value = data.name || "Customer not found";
        })
        .catch(error => {
          console.error('Error fetching customer:', error);
        });
    }
  }


function fetchCustomerName() {
    const email = document.getElementById("emaill").value;
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

  
  function fetchAmountToPay() {
      const email = document.getElementById("cust_email").value; // Get the email value
      const bookName = document.getElementById("book_namee").value; // Get the book name value

      if (email && bookName) {
          fetch(`/getAmountToPay?email=${encodeURIComponent(email)}&book_name=${encodeURIComponent(bookName)}`)
              .then(response => {
                  if (!response.ok) {
                      throw new Error('Network response was not ok');
                  }
                  return response.json(); // Parse the JSON response
              })
              .then(data => {
                  const amountField = document.getElementById("amount_to_be_paid");
                  if (data.amount) { // Assuming the response has an 'amount' field
                      amountField.value = data.amount; // Populate the amount field
                  } else {
                      alert("Amount not found for this book and customer.");
                      amountField.value = ''; // Clear the amount field if not found
                  }
              })
              .catch(error => {
                  console.error("Error fetching amount to pay:", error);
                  alert("An error occurred while fetching the amount. Please try again.");
              });
      } else {
          alert("Please enter both email and book name.");
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


// Add event listener for the Export to Excel button
document.getElementById('exportButton').addEventListener('click', function() {
    // Make a call to the API
    fetch('/excel', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/vnd.ms-excel',
        }
    })
    .then(response => {
        if (response.ok) {
            return response.blob(); // Convert response to a Blob
        } else {
            throw new Error('Failed to download file');
        }
    })
    .then(blob => {
        // Create a URL for the Blob and set it to a link to download
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'availableBooks.xlsx'; // Set the file name
        document.body.appendChild(a);
        a.click(); // Simulate a click on the link
        a.remove(); // Remove the link from the document
        window.URL.revokeObjectURL(url); // Clean up the URL object
    })
    .catch(error => {
        console.error('Error:', error);
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
			fetchAndUpdateEventList();
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


function fetchAndUpdateEventList() {
fetch("/events")
.then(response =>response.json())
.then(data =>{
const eventTableBody=document.getElementById("eventTableBody");

eventTableBody.innerHTML="";

data.forEach(events =>{
const newRow=document.createElement('tr');
newRow.innerHTML=`
                <td>${events.id}</td>
                <td>${events.event_name}</td>
                <td>${events.details}</td>
                <td>${events.venue}</td>
                <td>${events.event_date}</td>
		   <td>${events.time}</td>
             
            `;
eventTableBody.appendChild(newRow);

});
})
.catch(error =>console.error("Error fetching event list:", error));
}


// Search functionality
const customerSearchForm = document.getElementById("customerSearchForm");
const issuedBooksResultsSection = document.getElementById("issuedBooksResultsSection");
const issuedBooksTableBody = document.getElementById("issuedBooksTableBody");

customerSearchForm.addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent the default form submission

    const email = document.getElementById("customerEmail").value.trim(); // Get the email input
	if (!validateEmail(email)) {
				            alert("Please enter a valid email address.");
				            return;
				        }

    // Make a fetch request to search for issued books
    fetch(`/searchCust?email=${encodeURIComponent(email)}`) // Corrected endpoint
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log("the issued book data", data);
            if (data.length > 0) {
                // Clear any previous results
                issuedBooksTableBody.innerHTML = "";

                // Display the results in the table
                data.forEach(item => {
                    const issuedDate = new Date(item.issue_date); // Create a Date object
                    const formattedDate = issuedDate.toISOString().split('T')[0]; // Format to YYYY-MM-DD

                    const newRow = document.createElement('tr');
                    newRow.innerHTML = `
                        <td>${formattedDate}</td> <!-- Display formatted date -->
                        <td>${item.book_name}</td>
                    `;
                    issuedBooksTableBody.appendChild(newRow);
                });

                // Show the results section
                issuedBooksResultsSection.style.display = 'block';
            } else {
                alert("No issued books found for this email.");
                issuedBooksResultsSection.style.display = 'none'; // Hide table if no results
            }
        })
        .catch(error => {
            console.error("Error fetching issued books:", error);
            alert("An error occurred while fetching issued books. Please try again.");
            issuedBooksResultsSection.style.display = 'none'; // Hide table if there's an error
        });
});

    
});