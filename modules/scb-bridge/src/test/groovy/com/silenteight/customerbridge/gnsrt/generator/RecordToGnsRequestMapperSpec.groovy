package com.silenteight.customerbridge.gnsrt.generator

import com.silenteight.customerbridge.common.alertrecord.AlertRecord
import com.silenteight.customerbridge.common.gnsparty.GnsParty
import com.silenteight.customerbridge.common.hitdetails.model.Suspect

import spock.lang.Specification
import spock.lang.Unroll

import static java.lang.String.valueOf

class RecordToGnsRequestMapperSpec extends Specification {

  def 'should throw illegal state exception when hit details cannot be parsed'() {
    given:
    def alertRecord = AlertRecord.builder()
        .systemId('someSystemId')
        .build()
    def alertDto = AlertDto.builder()
        .details('')
        .systemId(alertRecord.systemId)
        .suspects([new Suspect(ofacId: '1')])
        .build()
    def objectUnderTest = RecordToGnsRequestMapper.builder()
        .alertedParty(GnsParty.empty())
        .record(alertRecord)
        .alerts([alertDto])
        .build()

    when:
    objectUnderTest.toGnsRtRequest()

    then:
    thrown(IllegalStateException)
  }

  @Unroll
  def 'should throw exception when filtered date is empty or invalid, #filteredDate'() {
    given:
    def alertRecord = AlertRecord.builder()
        .systemId('someSystemId')
        .filteredString(filteredDate)
        .build()
    def alertDto = AlertDto.builder()
        .details(someGnsHitDetails)
        .systemId(alertRecord.systemId)
        .suspects([new Suspect(ofacId: 'AS00115135')])
        .build()
    def objectUnderTest = RecordToGnsRequestMapper.builder()
        .alertedParty(GnsParty.empty())
        .record(alertRecord)
        .alerts([alertDto])
        .build()

    when:
    objectUnderTest.toGnsRtRequest()

    then:
    thrown(IllegalStateException)

    where:
    filteredDate << [null, '', 'abc']
  }

  def 'should map to gnsRt request'() {
    given:
    def alertRecord = AlertRecord.builder()
        .systemId('someSystemId')
        .filteredString('2019/01/21 19:22:29')
        .build()

    def suspect1 = new Suspect(ofacId: 'AS00115135')
    def suspect2 = new Suspect(ofacId: 'AS03727093')
    def suspect3 = new Suspect(ofacId: 'AM00001443')

    def alertDto = AlertDto.builder()
        .details(someGnsHitDetails)
        .systemId(alertRecord.systemId)
        .suspects([suspect1, suspect2, suspect3])
        .build()
    def objectUnderTest = RecordToGnsRequestMapper.builder()
        .alertedParty(GnsParty.empty())
        .record(alertRecord)
        .alerts([alertDto])
        .build()

    when:
    def result = objectUnderTest.toGnsRtRequest()

    then:
    def info = result.screenCustomerNameRes.screenCustomerNameResPayload.screenCustomerNameResInfo
    def immediateResponseData = info.immediateResponseData
    valueOf(immediateResponseData.immediateResponseTimestamp) == '2019-01-21T11:22:29Z'

    def alerts = immediateResponseData.alerts
    alerts.size() == 1

    def alert = alerts.first()
    def hitList = alert.hitList
    alert.alertId == alertRecord.systemId
    alert.watchlistType == "AM"
    hitList.size() == 3

    hitList.first().hitId == suspect1.ofacId
    hitList.first().hitDetails ==
        'T0ZBQyBJRDpBUzAwMTE1MTM1Ck1BVENIOiAwLjAwClRBRzogTkFNCk1BVENISU5HVEVYVDogTUFOVUVMIFbDgVpRVUVaClJFU1VMVDogKDApCgpCQVRDSDogMjAxOS8wMS8xN18wMDAzX0JEX1NDSUNfREVOWQpOQU1FOiBEciBNQU5VRUwgVsOBWlFVRVoKICBTeW5vbnltczogbm9uZSAKQUREUkVTUzogMTk2MSBTZXJyYW5vIENsaWZmCkVhc3QgR2VvcmdlLCBMQSAxNzI1MwogIFN5bm9ueW1zOiBub25lIApDSVRZOiBNT0dBRElTSFUKICBTeW5vbnltczogbm9uZSAKQ09VTlRSWTogU0dQCiAgU3lub255bXM6IG5vbmUgClNUQVRFOiAKICBTeW5vbnltczogbm9uZSAKT1JJR0lOOgpEQkNPVU5UUlkKREVTSUdOQVRJT046ClhYLURMClRZUEU6CkkKU0VBUkNIIENPREVTOgpub25lClVTRVIgREFUQSAxOgpBTlRJLU1PTkVZIExBVU5ERVJJTkcgLVNlbnNpdGl2ZSBDbGllbnQtQXNzb2NpYXRlZApVU0VSIERBVEEgMjoKbm9uZQpPRkZJQ0lBTCBSRUY6Cm5vbmUKUEFTU1BPUlQ6Cm5vbmUKQklDIENPREVTOgpub25lCk5BVElEOgpBMDA5MjMwMQpQTEFDRSBPRiBCSVJUSDoKQkFHSERBRCwgSVJBUQpEQVRFIE9GIEJJUlRIOgpub25lCk5BVElPTkFMSVRZOgpub25lCkFERElUSU9OQUwgSU5GT1M6Ckxpc3QgSUQ6IDQ0IC8gQ3JlYXRlIERhdGU6IDAzLzIxLzIwMDIgMDA6MDA6MDAgLyBMYXN0IFVwZGF0ZSBEYXRlOiAwMS8xOS8yMDE3IDE3OjEyOjUwIC8gUHJvZ3JhbTogVEFMSUJBTiAvIFRpdGxlOiBHT1ZFUk5PUiBPRiBTQVJJUFVMIFBST1ZJTkNFIFVOREVSIFRIRSBUQUxJQkFOIFJFR0lNRTsgTUFVTEFWSSAvIE5hdGlvbmFsaXR5OiBBZmdoYW5pc3RhbiAvIE90aGVySW5mb3JtYXRpb246IE1lbWJlciBvZiBUYWxpYmFuIFN1cHJlbWUgQ291bmNpbCBhcyBhdCAyMDExLiBCZWxvbmdzIHRvIE51cnphaSB0cmliZS4gSU5URVJQT0wtVU4gU2VjdXJpdHkgQ291bmNpbCBTcGVjaWFsIE5vdGljZSB3ZWIgbGluazogaHR0cHM6Ly93d3cuaW50ZXJwb2wuaW50L2VuL25vdGljZS9zZWFyY2gvdW4vNDY3ODU1MyAvIE9yaWdpbmFsSUQ6IFRBaS4xMDcKRk1MIFRZUEU6CjAKRk1MIFBSSU9SSVRZOgowCkZNTCBDT05GSURFTlRJQUxJVFk6CjAKRk1MIElORk86Cm5vbmUKUEVQLUZFUDoKMCAwCktFWVdPUkRTOgpub25lCkhZUEVSTElOS1M6Cm5vbmUKVFlTOiAyCklTTjogMAo='

    hitList.get(1).hitId == suspect2.ofacId
    hitList.get(1).hitDetails ==
        'T0ZBQyBJRDpBUzAzNzI3MDkzCk1BVENIOiAwLjAwClRBRzogTkFNCk1BVENISU5HVEVYVDogTUFOVUVMClJFU1VMVDogKDApCgpCQVRDSDogMjAxOS8wMS8xN18wMDAzX0JEX1NDSUNfREVOWQpOQU1FOiBNQU5VRUwKICBTeW5vbnltczogbm9uZSAKQUREUkVTUzogNDEwIEpvc2h1YSBNZWFkb3cgQXB0LiAxMzUKV2VzdCBBc2hsZXl2aWxsZSwgS1MgNzMzNzAKICBTeW5vbnltczogbm9uZSAKQ0lUWTogTm9uZQogIFN5bm9ueW1zOiBub25lIApDT1VOVFJZOiBVTklURUQgQVJBQiBFTUlSQVRFUwogIFN5bm9ueW1zOiBub25lIApTVEFURTogCiAgU3lub255bXM6IG5vbmUgCk9SSUdJTjoKVU4KREVTSUdOQVRJT046ClhYLURMClRZUEU6CkkKU0VBUkNIIENPREVTOgpub25lClVTRVIgREFUQSAxOgpBTlRJLU1PTkVZIExBVU5ERVJJTkctU2Vuc2l0aXZlIENsaWVudC1SZXN0cmljdGVkClVTRVIgREFUQSAyOgpub25lCk9GRklDSUFMIFJFRjoKbm9uZQpQQVNTUE9SVDoKbm9uZQpCSUMgQ09ERVM6Cm5vbmUKTkFUSUQ6CkEwMDkyMzAxClBMQUNFIE9GIEJJUlRIOgpCQUdIREFELCBJUkFRCkRBVEUgT0YgQklSVEg6CjE5ODMtMDMtMjQKTkFUSU9OQUxJVFk6Cm5vbmUKQURESVRJT05BTCBJTkZPUzoKTGlzdCBJRDogNDQgLyBDcmVhdGUgRGF0ZTogMDMvMjEvMjAwMiAwMDowMDowMCAvIExhc3QgVXBkYXRlIERhdGU6IDAxLzE5LzIwMTcgMTc6MTI6NTAgLyBQcm9ncmFtOiBUQUxJQkFOIC8gVGl0bGU6IEdPVkVSTk9SIE9GIFNBUklQVUwgUFJPVklOQ0UgVU5ERVIgVEhFIFRBTElCQU4gUkVHSU1FOyBNQVVMQVZJIC8gTmF0aW9uYWxpdHk6IEFmZ2hhbmlzdGFuIC8gT3RoZXJJbmZvcm1hdGlvbjogTWVtYmVyIG9mIFRhbGliYW4gU3VwcmVtZSBDb3VuY2lsIGFzIGF0IDIwMTEuIEJlbG9uZ3MgdG8gTnVyemFpIHRyaWJlLiBJTlRFUlBPTC1VTiBTZWN1cml0eSBDb3VuY2lsIFNwZWNpYWwgTm90aWNlIHdlYiBsaW5rOiBodHRwczovL3d3dy5pbnRlcnBvbC5pbnQvZW4vbm90aWNlL3NlYXJjaC91bi80Njc4NTUzIC8gT3JpZ2luYWxJRDogVEFpLjEwNwpGTUwgVFlQRToKMApGTUwgUFJJT1JJVFk6CjAKRk1MIENPTkZJREVOVElBTElUWToKMApGTUwgSU5GTzoKbm9uZQpQRVAtRkVQOgowIDAKS0VZV09SRFM6Cm5vbmUKSFlQRVJMSU5LUzoKbm9uZQpUWVM6IDAKSVNOOiAzCg=='

    hitList.last().hitId == suspect3.ofacId
    hitList.last().hitDetails ==
        'T0ZBQyBJRDpBTTAwMDAxNDQzCk1BVENIOiAwLjAwClRBRzogTkFNCk1BVENISU5HVEVYVDogTUFOVUVMClJFU1VMVDogKDApCgpCQVRDSDogMjAxOS8wMS8xN18wMDAzX0JEX1NDSUNfREVOWQpOQU1FOiBNQU5VRUwKICBTeW5vbnltczogbm9uZSAKQUREUkVTUzogNzc5MCBHcmVnb3J5IE1pc3Npb24gU3VpdGUgMTQxClNvbGlzZm9ydCwgS1kgNTM3NzgKICBTeW5vbnltczogbm9uZSAKQ0lUWTogTm9uZQogIFN5bm9ueW1zOiBub25lIApDT1VOVFJZOiBTSU5HQVBPUkUKICBTeW5vbnltczogbm9uZSAKU1RBVEU6IAogIFN5bm9ueW1zOiBub25lIApPUklHSU46CkRCQ09VTlRSWQpERVNJR05BVElPTjoKR1dMClRZUEU6CkkKU0VBUkNIIENPREVTOgpub25lClVTRVIgREFUQSAxOgpBTlRJLU1PTkVZIExBVU5ERVJJTkctRmluYW5jaWFsLU90aGVycwpVU0VSIERBVEEgMjoKbm9uZQpPRkZJQ0lBTCBSRUY6Cm5vbmUKUEFTU1BPUlQ6Cm5vbmUKQklDIENPREVTOgpub25lCk5BVElEOgpBMDA5MjMwMQpQTEFDRSBPRiBCSVJUSDoKQkFHSERBRCwgSVJBUQpEQVRFIE9GIEJJUlRIOgoxOTkyLTA3LTAxCk5BVElPTkFMSVRZOgpub25lCkFERElUSU9OQUwgSU5GT1M6Ckxpc3QgSUQ6IDQ0IC8gQ3JlYXRlIERhdGU6IDAzLzIxLzIwMDIgMDA6MDA6MDAgLyBMYXN0IFVwZGF0ZSBEYXRlOiAwMS8xOS8yMDE3IDE3OjEyOjUwIC8gUHJvZ3JhbTogVEFMSUJBTiAvIFRpdGxlOiBHT1ZFUk5PUiBPRiBTQVJJUFVMIFBST1ZJTkNFIFVOREVSIFRIRSBUQUxJQkFOIFJFR0lNRTsgTUFVTEFWSSAvIE5hdGlvbmFsaXR5OiBBZmdoYW5pc3RhbiAvIE90aGVySW5mb3JtYXRpb246IE1lbWJlciBvZiBUYWxpYmFuIFN1cHJlbWUgQ291bmNpbCBhcyBhdCAyMDExLiBCZWxvbmdzIHRvIE51cnphaSB0cmliZS4gSU5URVJQT0wtVU4gU2VjdXJpdHkgQ291bmNpbCBTcGVjaWFsIE5vdGljZSB3ZWIgbGluazogaHR0cHM6Ly93d3cuaW50ZXJwb2wuaW50L2VuL25vdGljZS9zZWFyY2gvdW4vNDY3ODU1MyAvIE9yaWdpbmFsSUQ6IFRBaS4xMDcKRk1MIFRZUEU6CjAKRk1MIFBSSU9SSVRZOgowCkZNTCBDT05GSURFTlRJQUxJVFk6CjAKRk1MIElORk86Cm5vbmUKUEVQLUZFUDoKMCAwCktFWVdPUkRTOgpub25lCkhZUEVSTElOS1M6Cm5vbmUKVFlTOiAwCklTTjogMAo='

  }

  def someGnsHitDetails = '''Suspect(s) detected by OFAC-Agent:3
SystemId: BD_SCIC_DENY!9BE06FFA-0058416D-929B432D-FB8BD10B
Associate: 
=============================
Suspect detected #1

OFAC ID:AS00115135
MATCH: 0.00
TAG: NAM
MATCHINGTEXT: MANUEL VÁZQUEZ
RESULT: (0)

BATCH: 2019/01/17_0003_BD_SCIC_DENY
NAME: Dr MANUEL VÁZQUEZ
  Synonyms: none 
ADDRESS: 1961 Serrano Cliff
East George, LA 17253
  Synonyms: none 
CITY: MOGADISHU
  Synonyms: none 
COUNTRY: SGP
  Synonyms: none 
STATE: 
  Synonyms: none 
ORIGIN:
DBCOUNTRY
DESIGNATION:
XX-DL
TYPE:
I
SEARCH CODES:
none
USER DATA 1:
ANTI-MONEY LAUNDERING -Sensitive Client-Associated
USER DATA 2:
none
OFFICIAL REF:
none
PASSPORT:
none
BIC CODES:
none
NATID:
A0092301
PLACE OF BIRTH:
BAGHDAD, IRAQ
DATE OF BIRTH:
none
NATIONALITY:
none
ADDITIONAL INFOS:
List ID: 44 / Create Date: 03/21/2002 00:00:00 / Last Update Date: 01/19/2017 17:12:50 / Program: TALIBAN / Title: GOVERNOR OF SARIPUL PROVINCE UNDER THE TALIBAN REGIME; MAULAVI / Nationality: Afghanistan / OtherInformation: Member of Taliban Supreme Council as at 2011. Belongs to Nurzai tribe. INTERPOL-UN Security Council Special Notice web link: https://www.interpol.int/en/notice/search/un/4678553 / OriginalID: TAi.107
FML TYPE:
0
FML PRIORITY:
0
FML CONFIDENTIALITY:
0
FML INFO:
none
PEP-FEP:
0 0
KEYWORDS:
none
HYPERLINKS:
none
TYS: 2
ISN: 0

=============================
Suspect detected #2

OFAC ID:AS03727093
MATCH: 0.00
TAG: NAM
MATCHINGTEXT: MANUEL
RESULT: (0)

BATCH: 2019/01/17_0003_BD_SCIC_DENY
NAME: MANUEL
  Synonyms: none 
ADDRESS: 410 Joshua Meadow Apt. 135
West Ashleyville, KS 73370
  Synonyms: none 
CITY: None
  Synonyms: none 
COUNTRY: UNITED ARAB EMIRATES
  Synonyms: none 
STATE: 
  Synonyms: none 
ORIGIN:
UN
DESIGNATION:
XX-DL
TYPE:
I
SEARCH CODES:
none
USER DATA 1:
ANTI-MONEY LAUNDERING-Sensitive Client-Restricted
USER DATA 2:
none
OFFICIAL REF:
none
PASSPORT:
none
BIC CODES:
none
NATID:
A0092301
PLACE OF BIRTH:
BAGHDAD, IRAQ
DATE OF BIRTH:
1983-03-24
NATIONALITY:
none
ADDITIONAL INFOS:
List ID: 44 / Create Date: 03/21/2002 00:00:00 / Last Update Date: 01/19/2017 17:12:50 / Program: TALIBAN / Title: GOVERNOR OF SARIPUL PROVINCE UNDER THE TALIBAN REGIME; MAULAVI / Nationality: Afghanistan / OtherInformation: Member of Taliban Supreme Council as at 2011. Belongs to Nurzai tribe. INTERPOL-UN Security Council Special Notice web link: https://www.interpol.int/en/notice/search/un/4678553 / OriginalID: TAi.107
FML TYPE:
0
FML PRIORITY:
0
FML CONFIDENTIALITY:
0
FML INFO:
none
PEP-FEP:
0 0
KEYWORDS:
none
HYPERLINKS:
none
TYS: 0
ISN: 3

=============================
Suspect detected #3

OFAC ID:AM00001443
MATCH: 0.00
TAG: NAM
MATCHINGTEXT: MANUEL
RESULT: (0)

BATCH: 2019/01/17_0003_BD_SCIC_DENY
NAME: MANUEL
  Synonyms: none 
ADDRESS: 7790 Gregory Mission Suite 141
Solisfort, KY 53778
  Synonyms: none 
CITY: None
  Synonyms: none 
COUNTRY: SINGAPORE
  Synonyms: none 
STATE: 
  Synonyms: none 
ORIGIN:
DBCOUNTRY
DESIGNATION:
GWL
TYPE:
I
SEARCH CODES:
none
USER DATA 1:
ANTI-MONEY LAUNDERING-Financial-Others
USER DATA 2:
none
OFFICIAL REF:
none
PASSPORT:
none
BIC CODES:
none
NATID:
A0092301
PLACE OF BIRTH:
BAGHDAD, IRAQ
DATE OF BIRTH:
1992-07-01
NATIONALITY:
none
ADDITIONAL INFOS:
List ID: 44 / Create Date: 03/21/2002 00:00:00 / Last Update Date: 01/19/2017 17:12:50 / Program: TALIBAN / Title: GOVERNOR OF SARIPUL PROVINCE UNDER THE TALIBAN REGIME; MAULAVI / Nationality: Afghanistan / OtherInformation: Member of Taliban Supreme Council as at 2011. Belongs to Nurzai tribe. INTERPOL-UN Security Council Special Notice web link: https://www.interpol.int/en/notice/search/un/4678553 / OriginalID: TAi.107
FML TYPE:
0
FML PRIORITY:
0
FML CONFIDENTIALITY:
0
FML INFO:
none
PEP-FEP:
0 0
KEYWORDS:
none
HYPERLINKS:
none
TYS: 0
ISN: 0

=============================
   *** INTERNAL OFAC DETAILS ***
HasSndRcvIn
Limited: 0
|AS00115135|0.00|NAM|22|30|63|64|
|AS03727093|0.00|NAM|22|30|63|64|
|AM00001443|0.00|NAM|22|30|63|64|
'''
}
