// Copyright (c) 2021, Christian Betancourt
// https://github.com/criistian14
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file or at
// https://opensource.org/licenses/MIT.

import 'dart:typed_data';

import 'package:document_scanner/src/models/filter_type.dart';
import 'package:equatable/equatable.dart';

/// Class to create events
abstract class EditEvent extends Equatable {}

/// Initialize the page with the image
class EditStarted extends EditEvent {
  /// Create an event instance
  EditStarted(this.image);

  /// Bytes of the image base
  final Uint8List image;

  @override
  List<Object?> get props => [
        image,
      ];
}

/// Apply the filter to the image
class EditFilterChanged extends EditEvent {
  /// Create an event instance
  EditFilterChanged(this.filter);

  /// Filter to apply
  final FilterType filter;

  @override
  List<Object?> get props => [
        filter,
      ];
}
