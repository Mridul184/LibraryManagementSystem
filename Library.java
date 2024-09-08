import java.io.*;
import java.util.*;

class Library {
    private ArrayList<Book> books;
    private HashMap<String, Date> issuedBooks; // Stores ISBN and issue date
    private final String dataFile = "library_data.ser";

    public Library() {
        books = new ArrayList<>();
        issuedBooks = new HashMap<>();
        loadData(); // Load data from file at startup
    }

    // Add a new book
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added successfully!");
    }

    // Issue a book to a student
    public void issueBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn) && !book.isIssued()) {
                book.setIssued(true);
                issuedBooks.put(isbn, new Date());
                System.out.println("Book issued successfully!");
                return;
            }
        }
        System.out.println("Book not available or already issued.");
    }

    // Return a book and calculate fine
    public void returnBook(String isbn) {
        if (issuedBooks.containsKey(isbn)) {
            Date issueDate = issuedBooks.get(isbn);
            Date currentDate = new Date();
            long diff = currentDate.getTime() - issueDate.getTime();
            long daysLate = diff / (1000 * 60 * 60 * 24);

            double fine = (daysLate > 14) ? (daysLate - 14) * 1.5 : 0; // Assuming Rs. 1.5 per day after 14 days
            issuedBooks.remove(isbn);
            for (Book book : books) {
                if (book.getIsbn().equals(isbn)) {
                    book.setIssued(false);
                    break;
                }
            }
            System.out.println("Book returned successfully! Fine: Rs. " + fine);
        } else {
            System.out.println("This book is not issued.");
        }
    }

    // Display all books in the library
    public void displayBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // Save data to a file
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(books);
            oos.writeObject(issuedBooks);
            System.out.println("Library data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving library data: " + e.getMessage());
        }
    }

    // Load data from a file
    public void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            books = (ArrayList<Book>) ois.readObject();
            issuedBooks = (HashMap<String, Date>) ois.readObject();
            System.out.println("Library data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading library data: " + e.getMessage());
        }
    }
}
