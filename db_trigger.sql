CREATE TABLE Message (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         customer VARCHAR(255),
                         message TEXT,
                         dateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER $$

CREATE TRIGGER after_transaction_insert_trigger
    AFTER INSERT ON banking
    FOR EACH ROW
BEGIN
    DECLARE customer_name VARCHAR(255);

    -- Retrieve the customer's full name
    SELECT CONCAT(first_name, ' ', last_name)
    INTO customer_name
    FROM customer
    WHERE id = NEW.customer_id;

    -- Insert the message into the Message table
    INSERT INTO Message (customer, message, dateTime)
    VALUES (customer_name, CONCAT('Transaction completed: ', NEW.amount), NOW());
END $$

DELIMITER ;
