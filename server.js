var io = require('socket.io').listen(80);


// usernames which are currently connected to the chat
var usernames = {};
var secretword = [];
var usercount = 0;
var wordlist = ["cat", "dog", "lion", "tiger", "snake", "eagle", "seal", "shark"];
var userid = [];

// rooms which are currently available in chat
var rooms = ['room1','room2','room3'];



	//per round timer
	var round=0;





io.sockets.on('connection', function (socket) {

	usercount++;
	

	if (round == 0 && usercount > 1) {
	round = setInterval(function(){
	console.log("running");
	var currentword= wordlist[Math.floor((Math.random()*wordlist.length))];
	var currentdrawer= userid[Math.floor((Math.random()*userid.length))];
	io.sockets.in("room1").emit('newround', currentdrawer.id);
	secretword["room1"] = currentword;
	currentdrawer.emit("newword", currentword);
	console.log(currentword + currentdrawer.id);
	},60000);

}



	// when the client emits 'adduser', this listens and executes
	socket.on('adduser', function(username){
		// store the username in the socket session for this client
		socket.username = username;
		// store the room name in the socket session for this client
		//socket.room = 'room1';
		// add the client's username to the global list
		usernames[username] = socket;
		userid.push(socket);
		// send client to room 1
		//socket.join('room1');
		// echo to client they've connected
		//socket.emit('updatechat', 'SERVER', 'you have connected to room1');
		// echo to room 1 that a person has connected to their room
		//io.sockets.in('room1').emit('updatechat', 'SERVER', username + ' has connected to this room');
		//socket.emit('updaterooms', rooms, 'room1');
	});



	// when the client emits 'adduser', this listens and executes
	socket.on('drawClick', function(username){
	//	 socket.broadcast.emit('draw', username);
		 socket.broadcast.to(socket.room).emit('draw', username);


	});


  	

	// create room
	socket.on('createroom', function(roomname){
		console.log(roomname.name);
		rooms[rooms.length] = roomname.name;
	});


	// channel list
	socket.on('getchannellist', function(roomname){
		var json = { };

		for (var i=0; i<rooms.length; i++) {
			json[rooms[i]] = "kategorinavn";
			
		}
		socket.emit('channellist', json);

	});




	// join room
	socket.on('joinroom', function(roomname){
		// store the username in the socket session for this client
		//socket.username = username;
		// store the room name in the socket session for this client
		socket.room = roomname;
		// add the client's username to the global list
		//usernames[username] = username;
		// send client to room 1
		socket.join(roomname);
		// echo to client they've connected
		//socket.emit('updatechat', 'SERVER', 'you have connected to');
		// echo to room 1 that a person has connected to their room
		io.sockets.in(roomname).emit('updatechat', 'SERVER', socket.username + ' has connected to this room');
		//socket.emit('updaterooms', rooms, 'room1');
	});
	


	// when the client emits 'sendchat', this listens and executes
	socket.on('guessword', function (data) {
		
		if (data.toString().toUpperCase() == [secretword[socket.room]].toString().toUpperCase()) {socket.emit("winner", "CONGRATS, YOU GUESSED RIGHT! YOU RECEIVED 100 POINTS")}
		io.sockets.in(socket.room).emit('updatechat', socket.username, data);
	});

	socket.on('switchRoom', function(newroom){
		// leave the current room (stored in session)
		socket.leave(socket.room);
		// join new room, received as function parameter
		socket.join(newroom);
		socket.emit('updatechat', 'SERVER', 'you have connected to '+ newroom);
		// sent message to OLD room
		socket.broadcast.to(socket.room).emit('updatechat', 'SERVER', socket.username+' has left this room');
		// update socket session room title
		socket.room = newroom;
		socket.broadcast.to(newroom).emit('updatechat', 'SERVER', socket.username+' has joined this room');
		socket.emit('updaterooms', rooms, newroom);
	});

	// when the user disconnects.. perform this
	socket.on('disconnect', function(){
		usercount--;
		if (usercount<2 && !(round == 0)) {clearInterval(round); round = 0;}
		// remove the username from global usernames list
		delete usernames[socket.username];
		userid.splice(userid.indexOf(socket),1);
		// update list of users in chat, client-side
		io.sockets.emit('updateusers', usernames);
		// echo globally that this client has left
		io.sockets.in('updatechat', 'SERVER', socket.username + ' has disconnected');
		socket.leave(socket.room);
	});
});
