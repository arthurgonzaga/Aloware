const callerNumber = '+5583993833323';
const callerIos = 'client:arthur-ios';
const callerAndroid = 'client:arthur-android';

exports.handler = function(context, event, callback) {
  const twiml = new Twilio.twiml.VoiceResponse();

  console.log("event -> " + event)

  var to =  (event.to) ? event.to : event.To;
  console.log("to -> " + to)
  if (!to) {
  	twiml.say('Congratulations! You have made your first call! Good bye.');
  } else if (isNumber(to)) {
  	const dial = twiml.dial({callerId : callerNumber});
    dial.number(to);
  } else if(to == "arthur-ios"){
  	const dial = twiml.dial({callerId : callerAndroid});
  	dial.client(to);
  } else if(to == "arthur-android"){
  	const dial = twiml.dial({callerId : callerIos});
  	dial.client(to);
  }

  callback(null, twiml);
};

function isNumber(to) {
  if(to.length == 1) {
    if(!isNaN(to)) {
      console.log("It is a 1 digit long number" + to);
      return true;
    }
  } else if(String(to).charAt(0) == '+') {
    number = to.substring(1);
    if(!isNaN(number)) {
      console.log("It is a number " + to);
      return true;
    };
  } else {
    if(!isNaN(to)) {
      console.log("It is a number " + to);
      return true;
    }
  }
  console.log("not a number");
  return false;
}