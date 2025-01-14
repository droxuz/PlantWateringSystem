%Andrew Nong
%Dec 5, 2022
%Watering a plant with volatage readings and water pump
clear all; close all; 
a = arduino('COM3','Uno');

figure(1)
h = animatedline;
ax = gca;
ax.YGrid = 'on';
ax.YLim = [-0.1 5];
title('Moisture Over Time');
xlabel('Time(HH:MM:SS)');
ylabel('Voltage(volt)');
i = 1;
stop = false;
startTime = datetime('now');
waterLevels=[];
while ~stop
    wetOrdry = readVoltage(a,'A0');
    waterLevels(i) = wetOrdry;
    time = datetime('now')-startTime;
    addpoints(h,datenum(time),wetOrdry)
    ax.XLim = datenum([time-seconds(15) time]);
    datetick('x','keeplimits')
    drawnow
    waterPump(wetOrdry,a)
    stop = readDigitalPin(a,'D6');
    i = i+1;
end
figure(2)
maxi = max(waterLevels);
mini = min(waterLevels);
plot(mini:maxi,0:1);

function waterPump(wetOrdry,a)
    if (wetOrdry >= 3.2)
        disp('Soil is dry.')
        writeDigitalPin(a,'D2',1); pause(1);writeDigitalPin(a,'D2',0)
        disp('Watering now.')
    elseif (wetOrdry<3.2 && wetOrdry>2.9)
        disp('Soil is a little wet.')
        writeDigitalPin(a,'D2',1); pause(1);writeDigitalPin(a,'D2',0)
        disp('Watering now.')
    elseif(wetOrdry<= 2.9)
        disp('The soil is moisturized')
        writeDigitalPin(a,'D2',0)
        disp('Stopping the watering')
    end
end