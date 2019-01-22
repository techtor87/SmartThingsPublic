/**
 *  Link Switch
 *
 *  Copyright 2018 GREG FAULCONBRIDGE
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Link Switch",
    namespace: "techtor87",
    author: "GREG FAULCONBRIDGE",
    description: "Link Switches to turn on multiple, creating a digital 3-way switch.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Master Switch") {
    	input "master", "capability.switch", required:true, title:"Where"
	}
    section("Slave Switch"){
    	input "switches", "capability.switch", multiple:true, required:false
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	subscribe(master, "switch", switchHandler, [filterEvents: false])
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	subscribe(master, "switch", switchHandler, [filterEvents: false])
}

def switchHandler(evt){
	log.debug "handler called"
    log.info evt.physical
    log.info evt.value
    
	if (evt.value == "on") {
        log.debug "detected on tap, turn on other light(s)"
        onSwitches()*.on()
    } else if (evt.value == "off") {
        log.debug "detected off tap, turn off other light(s)"
        offSwitches()*.off()
    }
}

private onSwitches() {
	(switches + onSwitches).findAll{it}
}

private offSwitches() {
	(switches + offSwitches).findAll{it}
}