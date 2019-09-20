import './enzyme.config.js';
import React from 'react';
import {mount} from 'enzyme';
import AboutUs from '../src/components/Application/AboutUs/AboutUs';


function testCreateAboutUsEntries() {

    const aboutUs = mount((
        <AboutUs/>
    ));

    let numberOfEntries = aboutUs.find('ListGroupItem').length;
    expect(numberOfEntries).toEqual(4);

    let actualEntries = [];
    aboutUs.find('ListGroupItem').map((entry) => actualEntries.push(entry.prop('name')));

    let expectedEntries = [
        'Garrett St. Amand',
        'Kyle Pashak',
        'Evan Counihan',
        'Patrick Lee'
    ];

    expect(actualEntries).toEqual(expectedEntries);
}

/* Tests that createForm() correctly renders 2 Input components */
test('Testing the createAboutUsEntries() function in AboutUs', testCreateAboutUsEntries);

