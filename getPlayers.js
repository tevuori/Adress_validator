const mc = require('minecraft-protocol');
const client = mc.createClient({
    host: process.argv[2],   // optional
    port: 25565,         // optional
    username: makeid(Math.floor(Math.random() * (8 - 4 + 1)) + 4),
    // optional; by default uses offline mode, if using a microsoft account, set to 'microsoft'
});

client.on('player_info', (packet) => {
    console.log(packet.data);
});
setTimeout(function() {
    client.end()
}, 1000);



function makeid(length) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const charactersLength = characters.length;
    let counter = 0;
    while (counter < length) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
        counter += 1;
    }
    return result;
}
