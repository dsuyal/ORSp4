function validateName(event) {
	const input = event.target;
	let value = input.value;

	// Regular expression to allow only alphabets and single spaces between
	// words
	const regex = /^[A-Za-z]+(?: [A-Za-z]+)*$/;

	// Check if the value matches the regex
	if (!regex.test(value)) {
		// Find the position of the first invalid character or multiple spaces
		const match = value.match(/[^A-Za-z ]| {2,}|^ /);
		if (match) {
			const invalidIndex = match.index;

			// Remove the invalid character
			value = value.slice(0, invalidIndex)
					+ value.slice(invalidIndex + 1);
		}
	}

	// Remove leading space if exists
	if (value.startsWith(" ")) {
		value = value.slice(1);
	}

	// Update the input value
	input.value = value;
}


function validateMobile(event) {
    const input = event.target;
    let value = input.value;

    // Remove any non-numeric characters
    value = value.replace(/\D/g, '');

    // Check if the first digit is not 6, 7, 8, or 9
    if (value.length > 0 && !/^[6789]/.test(value.charAt(0))) {
        input.value = ''; // Clear the input if it starts with any digit other than 6, 7, 8, or 9
        return;
    }

    // Check if the second digit is 0 or 1
    if (value.length > 1 && /^[01]/.test(value.charAt(1))) {
        input.value = value.charAt(0); // Keep only the first digit if the second digit is 0 or 1
        return;
    }

    // Limit input to 10 digits
    if (value.length > 10) {
        input.value = value.slice(0, 10); // Trim to 10 digits
    } else {
        input.value = value;
    }
}


function validateQuantity(event) {
	const input = event.target;

	// Remove all non-numeric characters and spaces
	let value = input.value.replace(/[^0-9]/g, '');

	// Limit the input to 10 digits
	if (value.length > 10) {
		value = value.slice(0, 10);
	}

	// Update the input value
	input.value = value;
}

function validatePrice(event) {
	const input = event.target;
	let value = input.value;

	// Remove all characters except digits and decimal point
	let cleanedValue = value.replace(/[^0-9.]/g, '');

	// Allow only a single decimal point
	const parts = cleanedValue.split('.');
	if (parts.length > 2) {
		cleanedValue = parts[0] + '.' + parts.slice(1).join('');
	}

	// Ensure no leading or trailing spaces
	cleanedValue = cleanedValue.trim();

	// Update the input value
	input.value = cleanedValue;
}
