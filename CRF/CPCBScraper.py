#!/usr/bin/env python

# Gujarat, Ahmedabad, Maninagar
import sys
import time

import selenium as se
import re
import string
from urllib.parse import urlparse 

from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.common.exceptions import NoSuchElementException
from bs4 import BeautifulSoup

class CPCBScraper(object):
    def __init__(self):
        self.url = 'http://cpcb.gov.in/CAAQM/frmUserAvgReportCriteria.aspx'
        options = se.webdriver.ChromeOptions()
        options.add_argument('headless')
        driver = se.webdriver.Chrome(chrome_options=options)
        self.driver = webdriver.PhantomJS()
        self.driver.set_window_size(1120, 550)

    def scrape(self):
        self.driver.get(self.url)

        #
        # State -> City -> Station
        # 
        state_select = Select(self.driver.find_element_by_id('ddlState'))
        state_option_indexes = range(1, len(state_select.options))

        # Iterate through each state
        for state_index in state_option_indexes:
            state_select = Select(self.driver.find_element_by_id('ddlState'))

            #
            # Selecting the state will cause the progress div to get reloaded
            # so we must record this before we select the state
            #
            old_progress_div = self.driver.find_element_by_id('UpdateProgress1')
            state_select.select_by_index(state_index)

            state = state_select.options[state_index].text
            print ('State'), state

            def done_loading(driver):
                time.sleep(1)
                div = driver.find_element_by_id('UpdateProgress1')

                print ('old_progress_div'), old_progress_div
                print ('new_progress_div'), div

                return div != old_progress_div

            # Wait until city loaded
            wait = WebDriverWait(self.driver, 10)
            wait.until(done_loading)

            city_select = Select(self.driver.find_element_by_id('ddlCity'))
            city_option_indexes = range(1, len(city_select.options))            

            for city_index in city_option_indexes:
                old_progress_div = self.driver.find_element_by_id('UpdateProgress1')

                city_select = Select(self.driver.find_element_by_id('ddlCity'))
                city_select.select_by_index(city_index)

                city = city_select.options[city_index].text
                print ('City'), city

                # Wait until station loaded
                wait = WebDriverWait(self.driver, 10)
                wait.until(done_loading)

                station_select = Select(self.driver.find_element_by_id('ddlStation'))
                station_option_indexes = range(1, len(station_select.options))            

                for station_index in station_option_indexes:
                    old_progress_div = self.driver.find_element_by_id('UpdateProgress1')

                    station_select = Select(self.driver.find_element_by_id('ddlStation'))
                    station_select.select_by_index(station_index)

                    station = station_select.options[station_index].text
                    print ('Station'), station

                    # Wait until parameters loaded
                    wait = WebDriverWait(self.driver, 10)
                    wait.until(done_loading)

                    # Select All and then click Add
                    param_select = Select(self.driver.find_element_by_id('lstBoxChannelLeft'))                    
                    param_select.select_by_value('All')
                    
                    old_progress_div = self.driver.find_element_by_id('UpdateProgress1')

                    add = self.driver.find_element_by_id('btnAdd')
                    add.click()
                    
                    # Wait until parameters loaded
                    wait = WebDriverWait(self.driver, 10)
                    wait.until(done_loading)

                    submit = self.driver.find_element_by_id('btnSubmit')
                    submit.click()

                    def report_loaded(driver):
                        form = driver.find_element_by_id('form1')
                        action = form.get_attribute('action')
                        return action.endswith('frmReportdisplay.aspx')

                    # Very slow load...
                    wait = WebDriverWait(self.driver, 600) 
                    wait.until(report_loaded)

                    self.driver.save_screenshot('-'.join([state, city, station, 'screenshot.png']))

                    # Click Close button to get back to form
                    close = self.driver.find_element_by_id('btnClose')
                    close.click()

                    def form_loaded(driver):
                        form = driver.find_element_by_id('form1')
                        action = form.get_attribute('action')
                        return action.endswith('frmUserAvgReportCriteria.aspx')
                        
                    wait = WebDriverWait(self.driver, 300) 
                    wait.until(form_loaded)

        self.driver.quit()

if __name__ == '__main__':
    scraper = CPCBScraper()
    scraper.scrape()
