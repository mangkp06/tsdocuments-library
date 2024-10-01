import 'dart:async';

import 'package:document_scanner/src/models/contour.dart';
import 'package:document_scanner/src/models/filter_type.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

class DocumentScanner {
  static const MethodChannel _channel =
      MethodChannel('document_scanner');

  /// Call this to start get Picture workflow.
  static Future<List<String>?> getPictures(
      {int noOfPages = 100, bool isGalleryImportAllowed = false}) async {
    Map<Permission, PermissionStatus> statuses = await [
      Permission.camera,
    ].request();
    if (statuses.containsValue(PermissionStatus.denied) ||
        statuses.containsValue(PermissionStatus.permanentlyDenied)) {
      throw Exception("Permission not granted");
    }
    
    final List<dynamic>? pictures = await _channel.invokeMethod('getPictures', {
      'noOfPages': noOfPages,
      'isGalleryImportAllowed': isGalleryImportAllowed
    });
    return pictures?.map((e) => e as String).toList();
  }

  static Future<List<String>?> selectDocuments(
      {int noOfPages = 100}) async {
    Map<Permission, PermissionStatus> statuses = await [
      Permission.mediaLibrary,
    ].request();
    if (statuses.containsValue(PermissionStatus.denied) ||
        statuses.containsValue(PermissionStatus.permanentlyDenied)) {
      throw Exception("Permission not granted");
    }

    final List<dynamic>? pictures = await _channel.invokeMethod('selectDocuments', {
      'noOfPages': noOfPages
    });
    return pictures?.map((e) => e as String).toList();
  }

  static Future<Contour?> findContourPhoto({
    required Uint8List byteData,
    required double minContourArea,
  }) async {
    final contour = await _channel.invokeMapMethod<String, dynamic>(
      'findContourPhoto',
      <String, Object>{
        'byteData': byteData,
        'minContourArea': minContourArea,
      },
    );

    if (contour != null) {
      return Contour.fromMap(contour);
    }

    return null;
  }

  static Future<Uint8List?> adjustingPerspective({
    required Uint8List byteData,
    required Contour contour,
  }) async {
    return _channel.invokeMethod<Uint8List>(
      'adjustingPerspective',
      <String, Object>{
        'byteData': byteData,
        'points': contour.points
            .map(
              (e) => {
                'x': e.x,
                'y': e.y,
              },
            )
            .toList(),
      },
    ).then((value) => value);
  }

  static Future<Uint8List?> applyFilter({
    required Uint8List byteData,
    required FilterType filter,
  }) async {
    return _channel.invokeMethod<Uint8List>(
      'applyFilter',
      <String, Object>{
        'byteData': byteData,
        'filter': filter.value,
      },
    ).then((value) => value);
  }
}
