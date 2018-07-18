export function formatDate(dateString) {
    const date = new Date(dateString);

    const monthNames = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'October',
        'November',
        'December',
    ];

    const monthIndex = date.getMonth();
    const year = date.getFullYear();

    return monthNames[monthIndex] + ' ' + year;
}

export function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);

    const monthNames = [
        'Jan',
        'Feb',
        'Mar',
        'Apr',
        'May',
        'Jun',
        'Jul',
        'Aug',
        'Sep',
        'Oct',
        'Nov',
        'Dec',
    ];

    const monthIndex = date.getMonth();
    const year = date.getFullYear();

    return (
        date.getDate() +
        ' ' +
        monthNames[monthIndex] +
        ' ' +
        year +
        ' - ' +
        date.getHours() +
        ':' +
        date.getMinutes()
    );
}

export function generateId() {
    var number = Math.random(); // 0.9394456857981651
    number.toString(36); // '0.xtis06h6'
    var id = number.toString(36).substr(2, 9); // 'xtis06h6'

    return id;
}
