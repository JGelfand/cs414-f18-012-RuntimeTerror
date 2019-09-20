import './enzyme.config.js';
import React from 'react';
import {mount} from 'enzyme';
import Calculator from '../src/components/Application/Calculator/Calculator';


const startProperties = {
  'options': {
    'units': {'miles': 3959, 'kilometers': 6371},
    'activeUnit': 'miles',
    'serverPort': 'black-bottle.cs.colostate.edu:31400'
  },
  'props':{
    'origin': {'latitude': '5', 'longitude': ''},
    'destination': {'latitude': '', 'longitude': ''},
  }
};

function testCreateInputFields() {

  const calculator = mount((
      <Calculator options={startProperties.options}
                  origin={startProperties.props.origin}
                  destination={startProperties.props.destination}
      />
  ));

  let numberOfInputs = calculator.find('Input').length;
  expect(numberOfInputs).toEqual(2);

  let actualInputs = [];
  calculator.find('Input').map((input) => actualInputs.push(input.prop('name')));

  let expectedInputs = [
    'originInput',
    'destinationInput'
  ];

  expect(actualInputs).toEqual(expectedInputs);
}

/* Tests that createForm() correctly renders 2 Input components */
test('Testing the createForm() function in Calculator', testCreateInputFields);

function testInputsOnChange() {
  const calculator = mount((
      <Calculator options={startProperties.options}
                  origin={startProperties.props.origin}
                  destination={startProperties.props.destination}
      />
  ));

  for (let inputIndex = 0; inputIndex < 2; inputIndex++){
    simulateOnChangeEvent(inputIndex, calculator);
  }

  expect(calculator.props().origin).toEqual(0);
  expect(calculator.props().destination).toEqual(1);

}

function simulateOnChangeEvent(inputIndex, reactWrapper) {
  let eventName = (inputIndex % 2 === 0) ? 'latitude' : 'longitude';
  let event = {target: {name: eventName, value: inputIndex}};
  switch(inputIndex) {
    case 0:
      reactWrapper.find('#originCoordinates').at(0).simulate('change', event);
      break;
    case 1:
      reactWrapper.find('#destinationCoordinates').at(0).simulate('change', event);
      break;
    default:
  }
  reactWrapper.update();
}

/* Loop through the Input indexes and simulate an onChange event with the index
 * as the input. To simulate the change, an event object needs to be created
 * with the name corresponding to its Input 'name' prop. Based on the index,
 * find the corresponding Input by its 'id' prop and simulate the change.
 *
 * Note: using find() with a prop as a selector for Inputs will return 2 objects:
 * 1: The function associated with the Input that is created by React
 * 2: The Input component itself
 *
 * The values in state() should be the ones assigned in the simulations.
 *
 * https://github.com/airbnb/enzyme/blob/master/docs/api/ShallowWrapper/simulate.md
 * https://airbnb.io/enzyme/docs/api/ReactWrapper/props.html
 * https://airbnb.io/enzyme/docs/api/ReactWrapper/find.html
 */
//test('Testing the onChange event of longitude Input in Calculator', testInputsOnChange);