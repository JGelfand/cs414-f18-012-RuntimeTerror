import './enzyme.config.js'
import React from 'react'
import {mount, shallow} from 'enzyme'
import Options from '../src/components/Application/Options/Options'
import Units from '../src/components/Application/Options/Units'


const startProperties = {
  'options': {
    'units': {'miles':3959, 'kilometers':6371},
    'activeUnit': 'miles'
  },
  'props':{
    'customUnitName' : '',
    'customUnitRadius': ''
  },
  'updateOption' : () => {}
};

function testRender() {
  const options = shallow(<Options options={startProperties.options}
                                   config={null}
                                   updateOption={startProperties.updateOption}/>);

  expect(options.contains(<Units options={startProperties.options}
                                 activeUnit={startProperties.options.activeUnit}
                                 updateOption={startProperties.updateOption}/>)).toEqual(true);
}

test('Check to see if a Units component is rendered', testRender);

function testInputFields() {
  const options = mount((
      <Options options={startProperties.options}
                  name={startProperties.props.customUnitName}
                  radius={startProperties.props.customUnitRadius}
      />
  ));

  let numberOfInputs = options.find('Input').length;
  expect(numberOfInputs).toEqual(2);

  let actualInputs = [];
  options.find('Input').map((input) => actualInputs.push(input.prop('name')));

  let expectedInputs = [
    'NameInput',
    'RadiusInput'
  ];

  expect(actualInputs).toEqual(expectedInputs);

}

test('Testing the createForm() function in Options', testInputFields);


